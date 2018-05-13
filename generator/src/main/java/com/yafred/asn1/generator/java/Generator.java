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
			processIntegerTypeAssignment((IntegerType)type, className);
		}
		else if(type.isBitStringType()) {
			processBitStringTypeAssignment((BitStringType)type, className);			
		}
		else if (type.isEnumeratedType()) {
			processEnumeratedTypeAssignment((EnumeratedType)type, className);			
		}		
		else if (type.isNullType()) {
			processBasicTypeAssignment(type);			
		}
		else  if (type.isBooleanType()) {
			processBasicTypeAssignment(type);
		}
		else  if (type.isOctetStringType()) {
			processBasicTypeAssignment(type);
		}	
		else  if (type.isObjectIdentifierType()) {
			processBasicTypeAssignment(type);
		}	
		else  if (type.isRelativeOIDType()) {
			processBasicTypeAssignment(type);
		}	
		else  if (type.isRestrictedCharacterStringType()) {
			processBasicTypeAssignment(type);
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
	
	
	private void processBasicTypeAssignment(Type type) throws Exception {
		String javaType = Utils.mapToJava(type);
		output.println("private " + javaType + " value;");
		output.println("public " + javaType + " getValue() { return value; }");
		output.println("public void setValue(" + javaType + " value) { this.value = value; }");
	}
	
	
	private void processIntegerTypeAssignment(IntegerType integerType, String className) throws Exception {
		String javaType = Utils.mapToJava(integerType);

		if (integerType.getNamedNumberList() != null) {
			for (NamedNumber namedNumber : integerType.getNamedNumberList()) {
				output.println("static final public " + javaType + " " + Utils.normalize(namedNumber.getName()) + " = new "
						+ javaType + "(" + namedNumber.getNumber() + ");");
			}
		}
		
		output.println("private " + javaType + " value;");
		output.println("public " + javaType + " getValue() { return value; }");
		output.println("public void setValue(" + javaType + " value) { this.value = value; }");
	}
	
	
	private void processBasicListElement(Type type) throws Exception {
		String javaType = Utils.mapToJava(type);

		output.println("private java.util.ArrayList<" + javaType + "> value;");
		output.println("public java.util.ArrayList<" + javaType + "> getValue() { return value; }");
		output.println("public void setValue(java.util.ArrayList<" + javaType + "> value) { this.value = value; }");
	}
	
	
	private void processIntegerListElement(IntegerType integerType) throws Exception {
		String javaType = Utils.mapToJava(integerType);

		if (integerType.getNamedNumberList() != null) {
			for (NamedNumber namedNumber : integerType.getNamedNumberList()) {
				output.println("static final public " + javaType + " " + Utils.normalize(namedNumber.getName()) + " = new "
						+ javaType + "(" + namedNumber.getNumber() + ");");
			}
		}
		
		output.println("private java.util.ArrayList<" + javaType + "> value;");
		output.println("public java.util.ArrayList<" + javaType + "> getValue() { return value; }");
		output.println("public void setValue(java.util.ArrayList<" + javaType + "> value) { this.value = value; }");
	}
	
	private void processEnumeratedTypeAssignment(EnumeratedType enumeratedType, String className) throws Exception {
		output.println("public enum Enum {");
	
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
		
		output.println("private Enum value;");
		output.println("public Enum getValue() { return value; }");
		output.println("public void setValue(Enum value) { this.value = value; }");
	}
	
	private void processBitStringTypeAssignment(BitStringType bitStringType, String className) throws Exception {	
		for(NamedNumber namedNumber : bitStringType.getNamedBitList()) {
			output.println("static final public int " + Utils.normalize(namedNumber.getName()) + "=" + namedNumber.getNumber() + ";");
		}
		
		String javaType = Utils.mapToJava(bitStringType);
		output.println("private " + javaType + " value;");
		output.println("public " + javaType + " getValue() { return value; }");
		output.println("public void setValue(" + javaType + " value) { this.value = value; }");
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
		Type type = listOfType.getElementType();
		if(type.isIntegerType()) {
			processIntegerListElement((IntegerType)listOfType.getElementType());
		}
		else if (type.isNullType() || type.isBooleanType() || type.isOctetStringType() || type.isRestrictedCharacterStringType() || type.isObjectIdentifierType() || type.isRelativeOIDType()) {
			processBasicListElement(type);			
		}
		else if(type.isBitStringType()) {
			processBitStringListElement((BitStringType)type);
		}
		else if(type.isEnumeratedType()) {
			processEnumeratedListElement((EnumeratedType)type);
		}
		else  if (type.isTypeReference()) {
			processTypeReferenceListElement((TypeReference)type);
		}	
		else {
			throw new Exception("Generator.processListOfTypeAssignment: Code generation not supported for Type " + listOfType.getElementType().getName());
		}
	}
	
	
	private void switchProcessNamedType(NamedType namedType) throws Exception {	
		String uComponentName = Utils.uNormalize(namedType.getName());
		String componentName = Utils.normalize(namedType.getName());
		Type type = namedType.getType();
		if (type.isIntegerType()) {
			processIntegerNamedType((IntegerType)type, componentName, uComponentName);
		} 
		else if (type.isEnumeratedType()) {
			processEnumeratedNamedType((EnumeratedType)type, componentName, uComponentName);
		}
		else if (type.isBitStringType()) {
			processBitStringNamedType((BitStringType)type, componentName, uComponentName);
		}
		else if (type.isNullType() || type.isBooleanType() || type.isOctetStringType() || type.isRestrictedCharacterStringType() || type.isObjectIdentifierType() || type.isRelativeOIDType()) {
			processBasicNamedType(type, componentName, uComponentName);			
		}
		else if (type.isTypeReference()) {
			processTypeReferenceNamedType((TypeReference)type, componentName, uComponentName);
		}	
		else if (type.isTypeWithComponents()) {
			processNestedTypeWithComponentsAssignment((TypeWithComponents)type, componentName);
		}		
		else 
			throw new Exception("Generator.switchProcessNamedType: Code generation not supported for component '" + componentName + "' of Type " + type.getName());
	}
	
	
	private void processBasicNamedType(Type type, String componentName, String uComponentName) throws Exception {
		String javaType = Utils.mapToJava(type);

		output.println("private " + javaType + " " + componentName + ";");
		output.println("public " + javaType + " get" + uComponentName +"() { return " + componentName + "; }");
		output.println("public void set" + uComponentName + "(" + javaType + " " + componentName + ") { this." + componentName + " = " + componentName + "; }");
	}
	
	
	private void processIntegerNamedType(IntegerType integerType, String componentName, String uComponentName) throws Exception {
		String javaType = Utils.mapToJava(integerType);

		if (integerType.getNamedNumberList() != null) {
			output.println("public static class " + uComponentName + "{");
			for (NamedNumber namedNumber : integerType.getNamedNumberList()) {
				output.println("static final public " + javaType + " " + Utils.normalize(namedNumber.getName()) + " = new "
						+ javaType + "(" + namedNumber.getNumber() + ");");
			}
			output.println("}");
		}
		
		output.println("private " + javaType + " " + componentName + ";");
		output.println("public " + javaType + " get" + uComponentName +"() { return " + componentName + "; }");
		output.println("public void set" + uComponentName + "(" + javaType + " " + componentName + ") { this." + componentName + " = " + componentName + "; }");
	}
	
	
	private void processEnumeratedListElement(EnumeratedType enumeratedType) throws Exception {
		String javaType = "Enum";
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
		
		output.println("private java.util.ArrayList<" + javaType + "> value;");
		output.println("public java.util.ArrayList<" + javaType + "> getValue() { return value; }");
		output.println("public void setValue(java.util.ArrayList<" + javaType + "> value) { this.value = value; }");
	}
	
	
	private void processEnumeratedNamedType(EnumeratedType enumeratedType, String componentName, String uComponentName) throws Exception {
		String javaType = uComponentName;
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
		
		output.println("private " + javaType + " " + componentName + ";");
		output.println("public " + javaType + " get" + uComponentName +"() { return " + componentName + "; }");
		output.println("public void set" + uComponentName + "(" + javaType + " " + componentName + ") { this." + componentName + " = " + componentName + "; }");
	}
	
	
	private void processBitStringListElement(BitStringType bitStringType) throws Exception {
		String javaType = Utils.mapToJava(bitStringType);

		for(NamedNumber namedNumber : bitStringType.getNamedBitList()) {
			output.println("static final public int " + Utils.normalize(namedNumber.getName()) + "=" + namedNumber.getNumber() + ";");
		}
		
		output.println("private java.util.ArrayList<" + javaType + "> value;");
		output.println("public java.util.ArrayList<" + javaType + "> getValue() { return value; }");
		output.println("public void setValue(java.util.ArrayList<" + javaType + "> value) { this.value = value; }");
	}
	
	
	private void processBitStringNamedType(BitStringType bitStringType, String componentName, String uComponentName) throws Exception {
		String javaType = Utils.mapToJava(bitStringType);

		output.println("public static class " + uComponentName + "{");
		for(NamedNumber namedNumber : bitStringType.getNamedBitList()) {
			output.println("static final public int " + Utils.normalize(namedNumber.getName()) + "=" + namedNumber.getNumber() + ";");
		}
		output.println("}");
		
		output.println("private " + javaType + " " + componentName + ";");
		output.println("public " + javaType + " get" + uComponentName +"() { return " + componentName + "; }");
		output.println("public void set" + uComponentName + "(" + javaType + " " + componentName + ") { this." + componentName + " = " + componentName + "; }");
	}
	
	
	private void processTypeReferenceListElement(TypeReference typeReference) throws Exception {
		String javaType = Utils.uNormalize(typeReference.getName());
		if(!Utils.isConstructed(typeReference.getBuiltinType()) && !typeReference.getBuiltinType().isEnumeratedType()) {
			javaType = Utils.mapToJava(typeReference.getBuiltinType());
		}

		if(typeReference.getBuiltinType().isEnumeratedType()) {
			output.println("private java.util.ArrayList<" + javaType + ".Enum> value;");
			output.println("public java.util.ArrayList<" + javaType + ".Enum> getValue() { return value; }");
			output.println("public void setValue(java.util.ArrayList<" + javaType + ".Enum> value) { this.value = value; }");
		}
		else {
			output.println("private java.util.ArrayList<" + javaType + "> value;");
			output.println("public java.util.ArrayList<" + javaType + "> getValue() { return value; }");
			output.println("public void setValue(java.util.ArrayList<" + javaType + "> value) { this.value = value; }");
		}
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
	

	private void processNestedTypeWithComponentsAssignment(TypeWithComponents type, String componentName) throws Exception {
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

}
