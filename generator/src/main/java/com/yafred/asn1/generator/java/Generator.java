package com.yafred.asn1.generator.java;


import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import com.google.googlejavaformat.java.Formatter;
import com.yafred.asn1.model.Assignment;
import com.yafred.asn1.model.BitStringType;
import com.yafred.asn1.model.BooleanType;
import com.yafred.asn1.model.ChoiceType;
import com.yafred.asn1.model.Component;
import com.yafred.asn1.model.EnumeratedType;
import com.yafred.asn1.model.IntegerType;
import com.yafred.asn1.model.ModuleDefinition;
import com.yafred.asn1.model.NamedNumber;
import com.yafred.asn1.model.NamedType;
import com.yafred.asn1.model.NullType;
import com.yafred.asn1.model.OctetStringType;
import com.yafred.asn1.model.RestrictedCharacterStringType;
import com.yafred.asn1.model.SequenceOfType;
import com.yafred.asn1.model.SequenceType;
import com.yafred.asn1.model.Specification;
import com.yafred.asn1.model.Type;
import com.yafred.asn1.model.TypeAssignment;
import com.yafred.asn1.model.TypeReference;

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
			berHelper.processTypeAssignment(typeAssignment.getType(), className);
			output.println("}");
		} else {
			output.println("public class " + className);
			output.println("{");
			switchProcessTypeAssignment(typeAssignment.getType(), className);
			output.println("}");
		}

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
			berHelper.processIntegerTypeAssignment((IntegerType)type, className);
		}
		else if(type.isBitStringType()) {
			processBitStringTypeAssignment((BitStringType)type, className);			
			berHelper.processBitStringTypeAssignment((BitStringType)type, className);
		}
		else if (type.isEnumeratedType()) {
			processEnumeratedTypeAssignment((EnumeratedType)type, className);			
			berHelper.processEnumeratedTypeAssignment((EnumeratedType)type, className);
		}		
		else if (type.isNullType()) {
			processBasicTypeAssignment(type);			
			berHelper.processNullTypeAssignment((NullType)type, className);
		}
		else  if (type.isBooleanType()) {
			processBasicTypeAssignment(type);
			berHelper.processBooleanTypeAssignment((BooleanType)type, className);
		}
		else  if (type.isOctetStringType()) {
			processBasicTypeAssignment(type);
			berHelper.processOctetStringTypeAssignment((OctetStringType)type, className);
		}	
		else  if (type.isRestrictedCharacterStringType()) {
			processBasicTypeAssignment(type);
			berHelper.processRestrictedCharacterStringTypeAssignment((RestrictedCharacterStringType)type, className);
		}	
		else if (type.isSequenceType()) {
			processSequenceTypeAssignment((SequenceType)type, className);
			berHelper.processSequenceTypeAssignment((SequenceType)type, className);
		}
		else if (type.isSequenceOfType()) {
			processSequenceOfTypeAssignment((SequenceOfType)type, className);
			berHelper.processSequenceOfTypeAssignment((SequenceOfType)type, className);
		}
		else if (type.isChoiceType()) {
			processChoiceTypeAssignment((ChoiceType)type, className);
			berHelper.processChoiceTypeAssignment((ChoiceType)type, className);
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

	private void processSequenceTypeAssignment(SequenceType sequenceType, String className) throws Exception {
		ArrayList<Component> componentList = new ArrayList<Component>();
		Utils.addAllIfNotNull(componentList, sequenceType.getRootComponentList());
		Utils.addAllIfNotNull(componentList, sequenceType.getExtensionComponentList());
		Utils.addAllIfNotNull(componentList, sequenceType.getAdditionalComponentList());

		for(Component component : componentList) {
			if(!component.isNamedType()) throw new Exception("Component can only be a NamedType here");
			NamedType namedType = (NamedType)component;
			switchProcessNamedType(namedType);
		}
	}
	
	private void processSequenceOfTypeAssignment(SequenceOfType sequenceOfType, String className) throws Exception {
		Type type = sequenceOfType.getElementType();
		if(type.isIntegerType()) {
			processIntegerListElement((IntegerType)sequenceOfType.getElementType());
		}
		else if (type.isNullType()) {
			processBasicListElement(type);			
		}
		else if (type.isBooleanType()) {
			processBasicListElement(type);
		}
		else if (type.isOctetStringType()) {
			processBasicListElement(type);
		}	
		else if (type.isRestrictedCharacterStringType()) {
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
			throw new Exception("Generator.processSequenceOfTypeAssignment: Code generation not supported for Type " + sequenceOfType.getElementType().getName());
		}
	}
	
	private void processChoiceTypeAssignment(ChoiceType choiceType, String className) throws Exception {
		ArrayList<Component> componentList = new ArrayList<Component>();
		Utils.addAllIfNotNull(componentList, choiceType.getRootAlternativeList());
		Utils.addAllIfNotNull(componentList, choiceType.getAdditionalAlternativeList());

		/* May be later
		output.println("public enum Choice {");
		boolean isFirst = true;
		for(Component component : componentList) {
			if(!component.isNamedType()) throw new Exception("Component can only be a NamedType here");
			NamedType namedType = (NamedType)component;
			if(!isFirst) {
				output.print(",");
			}
			output.println(Utils.normalize(namedType.getName()));
			isFirst = false;	
		}		
		
		output.println("}");
		*/
		
		for(Component component : componentList) {
			NamedType namedType = (NamedType)component;
			switchProcessNamedType(namedType);
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
		else if (type.isNullType()) {
			processBasicNamedType(type, componentName, uComponentName);			
		}
		else  if (type.isBooleanType()) {
			processBasicNamedType(type, componentName, uComponentName);
		}
		else  if (type.isOctetStringType()) {
			processBasicNamedType(type, componentName, uComponentName);
		}	
		else  if (type.isRestrictedCharacterStringType()) {
			processBasicNamedType(type, componentName, uComponentName);
		}	
		else  if (type.isTypeReference()) {
			processTypeReferenceNamedType((TypeReference)type, componentName, uComponentName);
		}	
		else 
			throw new Exception("Code generation not supported for component '" + componentName + "' of Type " + type.getName());
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
			output.println("private " + javaType + " " + componentName + ";");
			output.println("public " + javaType + ".Enum get" + uComponentName +"() { if(this." + componentName + "==null) return null; else return this." + componentName + ".getValue(); }");
			output.println("public void set" + uComponentName + "(" + javaType + ".Enum " + componentName + ") { this." + componentName + " = new " + javaType + "();  this." + componentName + ".setValue(" + componentName + ");}");			
		}
		else {
			output.println("private " + javaType + " " + componentName + ";");
			output.println("public " + javaType + " get" + uComponentName +"() { return " + componentName + "; }");
			output.println("public void set" + uComponentName + "(" + javaType + " " + componentName + ") { this." + componentName + " = " + componentName + "; }");
		}
	}
}
