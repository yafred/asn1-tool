/*******************************************************************************
 * Copyright (C) 2021 Fred D7e (https://github.com/yafred)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.yafred.asn1.generator.java;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.googlejavaformat.java.Formatter;
import com.yafred.asn1.model.Assignment;
import com.yafred.asn1.model.Component;
import com.yafred.asn1.model.ModuleDefinition;
import com.yafred.asn1.model.NamedNumber;
import com.yafred.asn1.model.Specification;
import com.yafred.asn1.model.Type;
import com.yafred.asn1.model.TypeAssignment;
import com.yafred.asn1.model.TypeWithComponents;
import com.yafred.asn1.model.type.BitStringType;
import com.yafred.asn1.model.type.EnumeratedType;
import com.yafred.asn1.model.type.IntegerType;
import com.yafred.asn1.model.type.ListOfType;
import com.yafred.asn1.model.type.NamedType;
import com.yafred.asn1.model.type.TypeReference;
import com.yafred.asn1.parser.Asn1SpecificationWriter;

public class Generator {
	Options options;
	File outputDir;
	String packageName;
	File packageDirectory;
	PrintWriter output;
	BERHelper berHelper;
	ASNValueHelper asnValueHelper;
	ValidationHelper validationHelper;
	
	
	public Generator() {
		berHelper = new BERHelper(this);
		asnValueHelper = new ASNValueHelper(this);
		validationHelper = new ValidationHelper(this);
	}
	
	
	public void setOptions(Options options) throws Exception {
		this.options = options;
		
		if(options.getPackagePrefix().length() > 0 && options.getPackagePrefix().substring(options.getPackagePrefix().length()-1) != ".") {
			options.setPackagePrefix(options.getPackagePrefix() + ".");
		}
		
		outputDir = new File(options.getOutputPath());

		if (!outputDir.exists()) {
			throw new Exception("Output directory does not exist: " + options.getOutputPath());
		}

		if (!outputDir.canWrite()) {
			throw new Exception("Cannot write output directory: " + options.getOutputPath());
		}
	}
	
	
	public void processSpecification(Specification specification) throws Exception {
		ArrayList<ModuleDefinition> moduleDefinitionList = specification.getModuleDefinitionList();

		for (ModuleDefinition moduleDefinition : moduleDefinitionList) {
			processModuleDefinition(moduleDefinition);
		}
	}

	
	private void processModuleDefinition(ModuleDefinition moduleDefinition) throws Exception {
		// create java package
		packageName = Utils.normalizePackageName(moduleDefinition.getModuleIdentifier().getModuleReference()).toLowerCase();
		packageDirectory = new File(outputDir, packageName);

		if (packageDirectory.exists()) {
			if (!options.isOverwriteAllowed()) {
				throw new Exception("Package Directory already exists: " + packageDirectory.getAbsolutePath());
			}
		} else {
			packageDirectory.mkdir();
		}

		List<Map.Entry<String,String>> typeMap = new ArrayList<Map.Entry<String,String>>();
		
		// process each type assignment
		for (Assignment assignment : moduleDefinition.getAssignmentList()) {
			if (assignment.isTypeAssignment()) {
				processTypeAssignment((TypeAssignment) assignment, typeMap);
			}
		}
		
		// Create a map file (ASN.1 references > Java classes)
		PrintWriter fileWriter = new PrintWriter(new FileWriter(new File(outputDir, "asn1-java.map")));
		fileWriter.println("[");
		boolean isFirst = true;
		for (Entry<String, String> entry : typeMap) {
			if(isFirst) {
				isFirst = false;
			}
			else {
				fileWriter.println(",");
			}
		    fileWriter.println("{ \"asn1\": \"" + entry.getKey() + "\", \"java\": \"" + entry.getValue() + "\" }");
		}
		fileWriter.println("]");
		fileWriter.close();
	}
	
	
	private void processTypeAssignment(TypeAssignment typeAssignment, List<Map.Entry<String,String>> typeMap) throws Exception {
		// get the ASN.1 spec for this type assignment
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Asn1SpecificationWriter asn1SpecificationWriter = new Asn1SpecificationWriter(new PrintStream(baos, true, "UTF-8"));
		asn1SpecificationWriter.visit(typeAssignment);
		String asn1Spec = new String(baos.toByteArray(), StandardCharsets.UTF_8);
		
		// systematically create a class
		String className = Utils.uNormalize(typeAssignment.getReference());

		StringWriter stringWriter = new StringWriter();
		output = new PrintWriter(stringWriter);

		output.println("package " + options.getPackagePrefix() + packageName + ";");
		
		Map.Entry<String,String> entry = new AbstractMap.SimpleEntry<String, String>(typeAssignment.getReference(), options.getPackagePrefix() + packageName + "." + className);
		typeMap.add(entry);

		if (typeAssignment.getType().isTypeReference()) {
			String parentClassName = Utils.normalizeJavaType((TypeReference) typeAssignment.getType(), options.getPackagePrefix());
			output.println("public class " + className + " extends " + parentClassName);
			output.println("{");
		} else {
			output.println("public class " + className);
			output.println("{");
			switchProcessTypeAssignment(typeAssignment.getType(), className);
		}

		// add encoding and decoding methods to the POJO
		this.addEncodingMethods(typeAssignment.getType(), className, false);

		output.println("}");
		output.close();
		
		String formattedSource = "";
		if(options.isBeautify()) {
		formattedSource = new Formatter().formatSource(stringWriter.getBuffer().toString());
		}
		else {
			formattedSource = stringWriter.getBuffer().toString();
		}
		
		PrintWriter fileWriter = new PrintWriter(new FileWriter(new File(packageDirectory, className + ".java")));
		fileWriter.println("/*");
		if(!options.getWatermark().equals("")) {
			fileWriter.println(options.getWatermark());
		}
		fileWriter.println(asn1Spec);
		fileWriter.println("*/");
		fileWriter.print(formattedSource);
		fileWriter.close();
	}

	
	private void switchProcessTypeAssignment(Type type, String className) throws Exception {		
		if (type.isIntegerType()) {
			processIntegerType((IntegerType)type, "value", false, false);
		}
		else if(type.isBitStringType()) {
			processBitStringType((BitStringType)type, "value", false, false);
		}
		else if (type.isEnumeratedType()) {
			processEnumeratedType((EnumeratedType)type, "value", "Enum", false);
		}		
		else if (type.isNullType() || type.isBooleanType() || type.isOctetStringType() || type.isObjectIdentifierType() || type.isRelativeOIDType() || type.isRestrictedCharacterStringType()) {
			processBasicType(type, "value", false);	
		}
		else if (type.isTypeWithComponents()) {
			processTypeWithComponentsAssignment((TypeWithComponents)type, className);
		}
		else if (type.isListOfType()) {
			processListOfTypeAssignment((ListOfType)type, className);
		}
		else {
			throw new Exception("Generator.switchProcessTypeAssignment: Code generation not supported for Type " + type.getName());
		}
	}
	
	
	private void processBasicType(Type type, String componentName, boolean isList) throws Exception {
		String uComponentName = Utils.uNormalize(componentName);
		String javaType = Utils.mapToJava(type);
		if(isList) {
			javaType = "java.util.ArrayList<" + javaType + ">";
		}

		output.println("private " + javaType + " _" + componentName + ";");
		output.println("public " + javaType + " get" + uComponentName +"() { return _" + componentName + "; }");
		output.println("public void set" + uComponentName + "(" + javaType + " _" + componentName + ") { this._" + componentName + " = _" + componentName + "; }");
	}
	
	
	private void processBitStringType(BitStringType bitStringType, String componentName, boolean isList, boolean isComponent) throws Exception {
		String uComponentName = Utils.uNormalize(componentName);
		String javaType = Utils.mapToJava(bitStringType);
		if(isList) {
			javaType = "java.util.ArrayList<" + javaType + ">";
		}
		
		if(isComponent) {
			// inner class
			output.println("static public class " + uComponentName + "{");
		}
		for(NamedNumber namedNumber : bitStringType.getNamedBitList()) {
			output.println("static final public int " + Utils.normalizeConstant(namedNumber.getName()) + "=" + namedNumber.getNumber() + ";");
		}
		if(isComponent) {
			output.println("}");
		}

		output.println("private " + javaType + " _" + componentName + ";");
		output.println("public " + javaType + " get" + uComponentName +"() { return _" + componentName + "; }");
		output.println("public void set" + uComponentName + "(" + javaType + " _" + componentName + ") { this._" + componentName + " = _" + componentName + "; }");
	}
	
	
	private void processEnumeratedType(EnumeratedType enumeratedType, String componentName, String javaType, boolean isList) throws Exception {
		String uComponentName = Utils.uNormalize(componentName);
		// inner class
		output.println("static public enum " + javaType + " {");
	
		boolean isFirst = true;
		for(NamedNumber namedNumber : enumeratedType.getRootEnumeration()) {
			if(!isFirst) {
				output.print(",");
			}
			output.println(Utils.normalizeConstant(namedNumber.getName()));
			isFirst = false;	
		}
		if(enumeratedType.getAdditionalEnumeration() != null) {
			for(NamedNumber namedNumber : enumeratedType.getAdditionalEnumeration()) {
				output.println("," + Utils.normalizeConstant(namedNumber.getName()));
			}
		}
		output.println("}");
		
		if(isList) {
			javaType = "java.util.ArrayList<" + javaType + ">";
		}
		output.println("private " + javaType + " _" + componentName + ";");
		output.println("public " + javaType + " get" + uComponentName +"() { return _" + componentName + "; }");
		output.println("public void set" + uComponentName + "(" + javaType + " _" + componentName + ") { this._" + componentName + " = _" + componentName + "; }");
	}
	
	
	private void processIntegerType(IntegerType integerType, String componentName, boolean isList, boolean isComponent) throws Exception {
		String uComponentName = Utils.uNormalize(componentName);
		String javaType = Utils.mapToJava(integerType);
		
		if (integerType.getNamedNumberList() != null) {
			if(isComponent) {
				// inner class
				output.println("static public class " + uComponentName + "{");
			}
			for (NamedNumber namedNumber : integerType.getNamedNumberList()) {
				output.println("static final public " + javaType + " " + Utils.normalizeConstant(namedNumber.getName()) + " = "
						+ javaType + ".valueOf(" + namedNumber.getNumber() + ");");
			}
			if(isComponent) {
				output.println("}");
			}
		}

		if(isList) {
			javaType = "java.util.ArrayList<" + javaType + ">";
		}
		output.println("private " + javaType + " _" + componentName + ";");
		output.println("public " + javaType + " get" + uComponentName +"() { return _" + componentName + "; }");
		output.println("public void set" + uComponentName + "(" + javaType + " _" + componentName + ") { this._" + componentName + " = _" + componentName + "; }");
	}
		
		
	private void processTypeWithComponentsAssignment(TypeWithComponents typeWithComponents, String className) throws Exception {
		ArrayList<Component> componentList = new ArrayList<Component>();
		Utils.addAllIfNotNull(componentList, typeWithComponents.getRootComponentList());
		Utils.addAllIfNotNull(componentList, typeWithComponents.getExtensionComponentList());
		Utils.addAllIfNotNull(componentList, typeWithComponents.getAdditionalComponentList());

		for(Component component : componentList) {
			if(!component.isNamedType()) throw new Exception("Generator.processTypeWithComponentsAssignment: Component can only be a NamedType here");
			NamedType namedType = (NamedType)component;
			switchProcessNamedType(namedType);
		}
	}
	
	
	private void processListOfTypeAssignment(ListOfType listOfType, String className) throws Exception {
		Type type = listOfType.getElement().getType();
		if(type.isIntegerType()) {
			processIntegerType((IntegerType)listOfType.getElement().getType(), "value", true, false);
		}
		else if(type.isBitStringType()) {
			processBitStringType((BitStringType)type, "value", true, false);
		}
		else if(type.isEnumeratedType()) {
			processEnumeratedType((EnumeratedType)type, "value", "Enum", true);
		}
		else if (type.isNullType() || type.isBooleanType() || type.isOctetStringType() || type.isRestrictedCharacterStringType() || type.isObjectIdentifierType() || type.isRelativeOIDType()) {
			processBasicType(type, "value", true);		
		}
		else if(type.isTypeWithComponents()) {
			String elementName = "item";
			if(listOfType.getElement().getName() != null && !listOfType.getElement().getName().equals("")) {
				elementName = Utils.normalize(listOfType.getElement().getName());
			}
			processTypeWithComponentsListElement((TypeWithComponents)type, "value", elementName);			
		}
		else if(type.isListOfType()) {
			String elementName = "item";
			if(listOfType.getElement().getName() != null && !listOfType.getElement().getName().equals("")) {
				elementName = Utils.normalize(listOfType.getElement().getName());
			}
			processListOfTypeListElement((ListOfType)type, "value", elementName);			
		}
		else if(type.isTypeReference()) {
			processTypeReferenceListElement((TypeReference)type, "value");
		}
		else {
			throw new Exception("Generator.processListOfTypeAssignment: Code generation not supported for Type " + listOfType.getElement().getType().getName());
		}
	}
	
	
	private void switchProcessNamedType(NamedType namedType) throws Exception {	
		String uComponentName = Utils.uNormalize(namedType.getName());
		String componentName = Utils.normalize(namedType.getName());
		Type type = namedType.getType();
		if (type.isIntegerType()) {
			processIntegerType((IntegerType)type, componentName, false, true);
		} 
		else if (type.isBitStringType()) {
			processBitStringType((BitStringType)type, componentName, false, true);
		}
		else if (type.isEnumeratedType()) {
			processEnumeratedType((EnumeratedType)type, componentName, uComponentName, false);
		}
		else if (type.isNullType() || type.isBooleanType() || type.isOctetStringType() || type.isRestrictedCharacterStringType() || type.isObjectIdentifierType() || type.isRelativeOIDType()) {
			processBasicType(type, componentName, false);
		}
		else if (type.isTypeWithComponents()) {
			processTypeWithComponentsNamedType((TypeWithComponents)type, componentName);
		}		
		else if (type.isListOfType()) {
			processListOfTypeNamedType((ListOfType)type, componentName);
		}		
		else if (type.isTypeReference()) {
			processTypeReferenceNamedType((TypeReference)type, componentName, uComponentName);
		}	
		else 
			throw new Exception("Generator.switchProcessNamedType: Code generation not supported for component '" + componentName + "' of Type " + type.getName());
	}
	
	
	private void processTypeReferenceListElement(TypeReference typeReference, String componentName) throws Exception {
		String uComponentName = Utils.uNormalize(componentName);

		String javaType = Utils.normalizeJavaType(typeReference, options.getPackagePrefix());
		if(!Utils.isConstructed(typeReference.getBuiltinType()) && !typeReference.getBuiltinType().isEnumeratedType()) {
			javaType = Utils.mapToJava(typeReference.getBuiltinType());
		}

		if(typeReference.getBuiltinType().isEnumeratedType()) {
			output.println("private java.util.ArrayList<" + javaType + ".Enum> _" + componentName + ";");
			output.println("public java.util.ArrayList<" + javaType + ".Enum> get" + uComponentName + "() { return _" + componentName + "; }");
			output.println("public void set" + uComponentName + "(java.util.ArrayList<" + javaType + ".Enum> _" + componentName + ") { this._" + componentName + " = _" + componentName + "; }");
		}
		else {
			output.println("private java.util.ArrayList<" + javaType + "> _" + componentName + ";");
			output.println("public java.util.ArrayList<" + javaType + "> get" + uComponentName + "() { return _" + componentName + "; }");
			output.println("public void set" + uComponentName + "(java.util.ArrayList<" + javaType + "> _" + componentName + ") { this._" + componentName + " = _" + componentName + "; }");
		}
	}
	
	
	private void processTypeWithComponentsListElement(TypeWithComponents typeWithComponents, String componentName, String elementName) throws Exception {
		String itemClassName = Utils.uNormalize(elementName);
		String uComponentName = Utils.uNormalize(componentName);
		
		// create accessors
		output.println("private java.util.ArrayList<" + itemClassName + "> _" + componentName + ";");
		output.println("public java.util.ArrayList<" + itemClassName + ">  get" + uComponentName +"() { return _" + componentName + "; }");
		output.println("public void set" + uComponentName + "(java.util.ArrayList<" + itemClassName + "> _" + componentName + ") { this._" + componentName + " = _" + componentName + "; }");

		// inner class
		output.println("static public class " + itemClassName);
		output.println("{");
		switchProcessTypeAssignment(typeWithComponents, itemClassName);

		// add encoding and decoding methods to the POJO
		this.addEncodingMethods(typeWithComponents, itemClassName, true);
		
		output.println("}");		
	}
	
	
	private void processListOfTypeListElement(ListOfType listOfType, String componentName, String elementName) throws Exception {
		String itemClassName = Utils.uNormalize(elementName);
		String uComponentName = Utils.uNormalize(componentName);
		
		// create accessors
		output.println("private java.util.ArrayList<" + itemClassName + "> _" + componentName + ";");
		output.println("public java.util.ArrayList<" + itemClassName + ">  get" + uComponentName +"() { return _" + componentName + "; }");
		output.println("public void set" + uComponentName + "(java.util.ArrayList<" + itemClassName + "> _" + componentName + ") { this._" + componentName + " = _" + componentName + "; }");

		// inner class
		output.println("static public class " + itemClassName);
		output.println("{");
		switchProcessTypeAssignment(listOfType, itemClassName);

		// add encoding and decoding methods to the POJO
		this.addEncodingMethods(listOfType, itemClassName, true);
		
		output.println("}");		
	}
	
	
	private void processTypeReferenceNamedType(TypeReference typeReference, String componentName, String uComponentName) throws Exception {
		String javaType = Utils.normalizeJavaType(typeReference, options.getPackagePrefix());
		if(!Utils.isConstructed(typeReference.getBuiltinType()) && !typeReference.getBuiltinType().isEnumeratedType()) {
			javaType = Utils.mapToJava(typeReference.getBuiltinType());
		}

		if(typeReference.getBuiltinType().isEnumeratedType()) {
			output.println("private " + javaType + ".Enum _" + componentName + ";");
			output.println("public " + javaType + ".Enum get" + uComponentName +"() { return this._" + componentName + "; }");
			output.println("public void set" + uComponentName + "(" + javaType + ".Enum _" + componentName + ") { this._" + componentName + " =  _" + componentName + ";}");			
		}
		else {
			output.println("private " + javaType + " _" + componentName + ";");
			output.println("public " + javaType + " get" + uComponentName +"() { return _" + componentName + "; }");
			output.println("public void set" + uComponentName + "(" + javaType + " _" + componentName + ") { this._" + componentName + " = _" + componentName + "; }");
			if(Utils.isConstructed(typeReference.getBuiltinType())) {
				output.println("public " + javaType + " set" + uComponentName + "() { if(this._" + componentName + "==null) this._" + componentName + "=new " + javaType + "();");
				output.println("return this._" + componentName + "; }");
			}
		}
	}
	

	private void processTypeWithComponentsNamedType(TypeWithComponents type, String componentName) throws Exception {
		String className = Utils.uNormalize(componentName);
		
		// create accessors
		output.println("private " + className + " _" + componentName + ";");
		output.println("public " + className + " get" + className +"() { return _" + componentName + "; }");
		output.println("public void set" + className + "(" + className + " _" + componentName + ") { this._" + componentName + " = _" + componentName + "; }");
		output.println("public " + className + " set" + className + "() { if(this._" + componentName + "==null) this._" + componentName + "=new " + className + "();");
		output.println("return this._" + componentName + "; }");

		// inner class
		/*
		 * Note: In case of multiple nesting levels, component names must be unique.
		 * SEQUENCE {
		 *   comp1 SEQUENCE {
		 *      comp1 SEQUENCE {
		 *      }
		 *   }
		 * }
		 * This will generate Java code that won't compile
		 */
		output.println("static public class " + className);
		output.println("{");
		switchProcessTypeAssignment(type, className);

		// add encoding and decoding methods to the POJO
		this.addEncodingMethods(type, className, true);
		
		output.println("}");
	}
	
	
	private void processListOfTypeNamedType(ListOfType listOfType, String componentName) throws Exception {
		Type type = listOfType.getElement().getType();
		if(type.isIntegerType()) {
			processIntegerType((IntegerType)listOfType.getElement().getType(), componentName, true, false);
		}
		else if(type.isBitStringType()) {
			processBitStringType((BitStringType)type, componentName, true, false);
		}
		else if(type.isEnumeratedType()) {
			processEnumeratedType((EnumeratedType)type, componentName, "Enum", true);
		}
		else if (type.isNullType() || type.isBooleanType() || type.isOctetStringType() || type.isRestrictedCharacterStringType() || type.isObjectIdentifierType() || type.isRelativeOIDType()) {
			processBasicType(type, componentName, true);		
		}
		else if(type.isTypeWithComponents()) {
			String elementName = "item";
			if(listOfType.getElement().getName() != null && !listOfType.getElement().getName().equals("")) {
				elementName = Utils.normalize(listOfType.getElement().getName());
			}
			processTypeWithComponentsListElement((TypeWithComponents)type, componentName, elementName);			
		}
		else if(type.isListOfType()) {
			String elementName = "item";
			if(listOfType.getElement().getName() != null && !listOfType.getElement().getName().equals("")) {
				elementName = Utils.normalize(listOfType.getElement().getName());
			}
			processListOfTypeListElement((ListOfType)type, componentName, elementName);			
		}
		else  if (type.isTypeReference()) {
			processTypeReferenceListElement((TypeReference)type, componentName);
		}	
		else {
			throw new Exception("Generator.processListOfTypeNamedType: Code generation not supported for Type " + listOfType.getElement().getType().getName());
		}
	}
	
	
	private void addEncodingMethods(Type type, String className, boolean isInnerType) throws Exception {
		// add BER methods to the POJO
		berHelper.processType(type, className, isInnerType);
		// add ASN value methods to the POJO
		asnValueHelper.processType(type, className, isInnerType);		
		// add validation methods to the POJO
		validationHelper.processType(type, className, isInnerType);		
	}

}
