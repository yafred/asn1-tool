package com.yafred.asn1.generator.java;


import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import com.google.googlejavaformat.java.Formatter;
import com.yafred.asn1.model.Assignment;
import com.yafred.asn1.model.BitStringType;
import com.yafred.asn1.model.ChoiceType;
import com.yafred.asn1.model.Component;
import com.yafred.asn1.model.EnumeratedType;
import com.yafred.asn1.model.IntegerType;
import com.yafred.asn1.model.ListOfType;
import com.yafred.asn1.model.ModuleDefinition;
import com.yafred.asn1.model.NamedNumber;
import com.yafred.asn1.model.NamedType;
import com.yafred.asn1.model.SequenceType;
import com.yafred.asn1.model.SetType;
import com.yafred.asn1.model.Specification;
import com.yafred.asn1.model.Type;
import com.yafred.asn1.model.TypeAssignment;
import com.yafred.asn1.model.TypeReference;
import com.yafred.asn1.model.TypeWithComponents;

public class Generator {
	Options options;
	File outputDir;
	String packageName;
	File packageDirectory;
	PrintWriter output;
	BERHelper berHelper;
	
	
	public Generator() {
		berHelper = new BERHelper(this);
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
		packageName = Utils.normalize(moduleDefinition.getModuleIdentifier().getModuleReference()).toLowerCase();
		packageDirectory = new File(outputDir, packageName);

		if (packageDirectory.exists()) {
			if (!options.isOverwriteAllowed()) {
				throw new Exception("Package Directory already exists: " + packageDirectory.getAbsolutePath());
			}
		} else {
			packageDirectory.mkdir();
		}

		// process each type assignment
		for (Assignment assignment : moduleDefinition.getAssignmentList()) {
			if (assignment.isTypeAssignment()) {
				processTypeAssignment((TypeAssignment) assignment);
			}
		}
	}
	
	
	private void processTypeAssignment(TypeAssignment typeAssignment) throws Exception {
		// systematically create a class
		String className = Utils.uNormalize(typeAssignment.getReference());

		StringWriter stringWriter = new StringWriter();
		output = new PrintWriter(stringWriter);

		output.println("package " + options.getPackagePrefix() + packageName + ";");

		if (typeAssignment.getType().isTypeReference()) {
			String parentClassName = Utils.uNormalize(((TypeReference) typeAssignment.getType()).getName());
			output.println("public class " + className + " extends " + parentClassName);
			output.println("{");
		} else {
			output.println("public class " + className);
			output.println("{");
			switchProcessTypeAssignment(typeAssignment.getType(), className);
		}

		// add BER methods to the POJO
		berHelper.switchProcessTypeAssignment(typeAssignment.getType(), className);
		
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
		else if (type.isNullType()) {
			processBasicType(type, "value", false);	
		}
		else  if (type.isBooleanType()) {
			processBasicType(type, "value", false);	
		}
		else  if (type.isOctetStringType()) {
			processBasicType(type, "value", false);	
		}	
		else  if (type.isObjectIdentifierType()) {
			processBasicType(type, "value", false);	
		}	
		else  if (type.isRelativeOIDType()) {
			processBasicType(type, "value", false);	
		}	
		else  if (type.isRestrictedCharacterStringType()) {
			processBasicType(type, "value", false);	
		}	
		else if (type.isSequenceType()) {
			processTypeWithComponentsAssignment((SequenceType)type, className);
		}
		else if (type.isSetType()) {
			processTypeWithComponentsAssignment((SetType)type, className);
		}
		else if (type.isListOfType()) {
			processListOfTypeAssignment((ListOfType)type, className);
		}
		else if (type.isChoiceType()) {
			processTypeWithComponentsAssignment((ChoiceType)type, className);
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

		output.println("private " + javaType + " " + componentName + ";");
		output.println("public " + javaType + " get" + uComponentName +"() { return " + componentName + "; }");
		output.println("public void set" + uComponentName + "(" + javaType + " " + componentName + ") { this." + componentName + " = " + componentName + "; }");
	}
	
	
	private void processBitStringType(BitStringType bitStringType, String componentName, boolean isList, boolean createInnerClass) throws Exception {
		String uComponentName = Utils.uNormalize(componentName);
		String javaType = Utils.mapToJava(bitStringType);
		if(isList) {
			javaType = "java.util.ArrayList<" + javaType + ">";
		}
		
		if(createInnerClass) {
			output.println("public static class " + uComponentName + "{");
		}
		for(NamedNumber namedNumber : bitStringType.getNamedBitList()) {
			output.println("static final public int " + Utils.normalize(namedNumber.getName()) + "=" + namedNumber.getNumber() + ";");
		}
		if(createInnerClass) {
			output.println("}");
		}

		output.println("private " + javaType + " " + componentName + ";");
		output.println("public " + javaType + " get" + uComponentName +"() { return " + componentName + "; }");
		output.println("public void set" + uComponentName + "(" + javaType + " " + componentName + ") { this." + componentName + " = " + componentName + "; }");
	}
	
	
	private void processEnumeratedType(EnumeratedType enumeratedType, String componentName, String javaType, boolean isList) throws Exception {
		String uComponentName = Utils.uNormalize(componentName);
		output.println("static public enum " + javaType + " {");
	
		boolean isFirst = true;
		for(NamedNumber namedNumber : enumeratedType.getRootEnumeration()) {
			if(!isFirst) {
				output.print(",");
			}
			output.println(Utils.normalize(namedNumber.getName()));
			isFirst = false;	
		}
		if(enumeratedType.getAdditionalEnumeration() != null) {
			for(NamedNumber namedNumber : enumeratedType.getAdditionalEnumeration()) {
				output.println("," + Utils.normalize(namedNumber.getName()));
			}
		}
		output.println("}");
		
		if(isList) {
			javaType = "java.util.ArrayList<" + javaType + ">";
		}
		output.println("private " + javaType + " " + componentName + ";");
		output.println("public " + javaType + " get" + uComponentName +"() { return " + componentName + "; }");
		output.println("public void set" + uComponentName + "(" + javaType + " " + componentName + ") { this." + componentName + " = " + componentName + "; }");
	}
	
	
	private void processIntegerType(IntegerType integerType, String componentName, boolean isList, boolean createInnerClass) throws Exception {
		String uComponentName = Utils.uNormalize(componentName);
		String javaType = Utils.mapToJava(integerType);
		
		if (integerType.getNamedNumberList() != null) {
			if(createInnerClass) {
				output.println("public static class " + uComponentName + "{");
			}
			for (NamedNumber namedNumber : integerType.getNamedNumberList()) {
				output.println("static final public " + javaType + " " + Utils.normalize(namedNumber.getName()) + " = new "
						+ javaType + "(" + namedNumber.getNumber() + ");");
			}
			if(createInnerClass) {
				output.println("}");
			}
		}

		if(isList) {
			javaType = "java.util.ArrayList<" + javaType + ">";
		}
		output.println("private " + javaType + " " + componentName + ";");
		output.println("public " + javaType + " get" + uComponentName +"() { return " + componentName + "; }");
		output.println("public void set" + uComponentName + "(" + javaType + " " + componentName + ") { this." + componentName + " = " + componentName + "; }");
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
		else if (type.isNullType() || type.isBooleanType() || type.isOctetStringType() || type.isRestrictedCharacterStringType() || type.isObjectIdentifierType() || type.isRelativeOIDType()) {
			processBasicType(type, "value", true);		
		}
		else if(type.isBitStringType()) {
			processBitStringType((BitStringType)type, "value", true, false);
		}
		else if(type.isEnumeratedType()) {
			processEnumeratedType((EnumeratedType)type, "value", "Enum", true);
		}
		else if(type.isTypeReference()) {
			processTypeReferenceListElement((TypeReference)type, "value");
		}
		else if(type.isTypeWithComponents()) {
			processTypeWithComponentsListElement((TypeWithComponents)type, "value", "item");			
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
		else if (type.isEnumeratedType()) {
			processEnumeratedType((EnumeratedType)type, componentName, uComponentName, false);
		}
		else if (type.isBitStringType()) {
			processBitStringType((BitStringType)type, componentName, false, true);
		}
		else if (type.isNullType() || type.isBooleanType() || type.isOctetStringType() || type.isRestrictedCharacterStringType() || type.isObjectIdentifierType() || type.isRelativeOIDType()) {
			processBasicType(type, componentName, false);
		}
		else if (type.isTypeReference()) {
			processTypeReferenceNamedType((TypeReference)type, componentName, uComponentName);
		}	
		else if (type.isTypeWithComponents()) {
			processTypeWithComponentsNamedType((TypeWithComponents)type, componentName);
		}		
		else if (type.isListOfType()) {
			processListOfTypeNamedType((ListOfType)type, componentName);
		}		
		else 
			throw new Exception("Generator.switchProcessNamedType: Code generation not supported for component '" + componentName + "' of Type " + type.getName());
	}
	
	
	private void processTypeReferenceListElement(TypeReference typeReference, String componentName) throws Exception {
		String uComponentName = Utils.uNormalize(componentName);

		String javaType = Utils.uNormalize(typeReference.getName());
		if(!Utils.isConstructed(typeReference.getBuiltinType()) && !typeReference.getBuiltinType().isEnumeratedType()) {
			javaType = Utils.mapToJava(typeReference.getBuiltinType());
		}

		if(typeReference.getBuiltinType().isEnumeratedType()) {
			output.println("private java.util.ArrayList<" + javaType + ".Enum> " + componentName + ";");
			output.println("public java.util.ArrayList<" + javaType + ".Enum> get" + uComponentName + "() { return " + componentName + "; }");
			output.println("public void set" + uComponentName + "(java.util.ArrayList<" + javaType + ".Enum> " + componentName + ") { this." + componentName + " = " + componentName + "; }");
		}
		else {
			output.println("private java.util.ArrayList<" + javaType + "> " + componentName + ";");
			output.println("public java.util.ArrayList<" + javaType + "> get" + uComponentName + "() { return " + componentName + "; }");
			output.println("public void set" + uComponentName + "(java.util.ArrayList<" + javaType + "> " + componentName + ") { this." + componentName + " = " + componentName + "; }");
		}
	}
	
	
	private void processTypeWithComponentsListElement(TypeWithComponents typeWithComponents, String componentName, String elementName) throws Exception {
		String itemClassName = Utils.uNormalize(elementName);
		String uComponentName = Utils.uNormalize(componentName);
		
		// create accessors
		output.println("private java.util.ArrayList<" + itemClassName + "> " + componentName + ";");
		output.println("public java.util.ArrayList<" + itemClassName + ">  get" + uComponentName +"() { return " + componentName + "; }");
		output.println("public void set" + uComponentName + "(java.util.ArrayList<" + itemClassName + "> " + componentName + ") { this." + componentName + " = " + componentName + "; }");

		// create an inner class
		output.println("static public class " + itemClassName);
		output.println("{");
		switchProcessTypeAssignment(typeWithComponents, itemClassName);

		// add BER methods to the POJO
		berHelper.switchProcessTypeAssignment(typeWithComponents, itemClassName);
		
		output.println("}");		
	}
	
	
	private void processTypeReferenceNamedType(TypeReference typeReference, String componentName, String uComponentName) throws Exception {
		String javaType = Utils.uNormalize(typeReference.getName());
		if(!Utils.isConstructed(typeReference.getBuiltinType()) && !typeReference.getBuiltinType().isEnumeratedType()) {
			javaType = Utils.mapToJava(typeReference.getBuiltinType());
		}

		if(typeReference.getBuiltinType().isEnumeratedType()) {
			output.println("private " + javaType + ".Enum " + componentName + ";");
			output.println("public " + javaType + ".Enum get" + uComponentName +"() { return this." + componentName + "; }");
			output.println("public void set" + uComponentName + "(" + javaType + ".Enum " + componentName + ") { this." + componentName + " = " + componentName + ";}");			
		}
		else {
			output.println("private " + javaType + " " + componentName + ";");
			output.println("public " + javaType + " get" + uComponentName +"() { return " + componentName + "; }");
			output.println("public void set" + uComponentName + "(" + javaType + " " + componentName + ") { this." + componentName + " = " + componentName + "; }");
			if(Utils.isConstructed(typeReference.getBuiltinType())) {
				output.println("public " + javaType + " set" + uComponentName + "() { if(this." + componentName + "==null) this." + componentName + "=new " + javaType + "();");
				output.println("return this." + componentName + "; }");
			}
		}
	}
	

	private void processTypeWithComponentsNamedType(TypeWithComponents type, String componentName) throws Exception {
		String className = Utils.uNormalize(componentName);
		
		// create accessors
		output.println("private " + className + " " + componentName + ";");
		output.println("public " + className + " get" + className +"() { return " + componentName + "; }");
		output.println("public void set" + className + "(" + className + " " + componentName + ") { this." + componentName + " = " + componentName + "; }");
		output.println("public " + className + " set" + className + "() { if(this." + componentName + "==null) this." + componentName + "=new " + className + "();");
		output.println("return this." + componentName + "; }");

		// create an inner class
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

		// add BER methods to the POJO
		berHelper.switchProcessTypeAssignment(type, className);
		
		output.println("}");
	}
	
	
	private void processListOfTypeNamedType(ListOfType listOfType, String componentName) throws Exception {
		Type type = listOfType.getElement().getType();
		if(type.isIntegerType()) {
			processIntegerType((IntegerType)listOfType.getElement().getType(), componentName, true, false);
		}
		else if (type.isNullType() || type.isBooleanType() || type.isOctetStringType() || type.isRestrictedCharacterStringType() || type.isObjectIdentifierType() || type.isRelativeOIDType()) {
			processBasicType(type, componentName, true);		
		}
		else if(type.isBitStringType()) {
			processBitStringType((BitStringType)type, componentName, true, false);
		}
		else if(type.isEnumeratedType()) {
			processEnumeratedType((EnumeratedType)type, componentName, "Enum", true);
		}
		else  if (type.isTypeReference()) {
			processTypeReferenceListElement((TypeReference)type, componentName);
		}	
		else {
			throw new Exception("Generator.processListOfTypeAssignment: Code generation not supported for Type " + listOfType.getElement().getType().getName());
		}
	}

}
