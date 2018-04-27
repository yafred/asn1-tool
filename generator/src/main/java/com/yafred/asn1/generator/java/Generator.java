package com.yafred.asn1.generator.java;


import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

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
import com.yafred.asn1.model.SequenceType;
import com.yafred.asn1.model.Specification;
import com.yafred.asn1.model.Type;
import com.yafred.asn1.model.TypeAssignment;
import com.yafred.asn1.model.TypeReference;

public class Generator {
	Options options;
	File outputDir;
	String packagePrefix = "";
	String packageName;
	File packageDirectory;
	PrintWriter output;
	BERHelper berHelper;
	
	public Generator() {
		berHelper = new BERHelper(this);
	}
	
	public void setPackagePrefix(String packagePrefix) {
		if(packagePrefix.substring(packagePrefix.length()-1) != ".") {
			packagePrefix += ".";
		}
		this.packagePrefix = packagePrefix;
	}
	
	public void setOutputDir(String outputPath) throws Exception {
		outputDir = new File(outputPath);

		if (!outputDir.exists()) {
			throw new Exception("Output directory does not exist: " + outputPath);
		}

		if (!outputDir.canWrite()) {
			throw new Exception("Cannot write output directory: " + outputPath);
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

		output = new PrintWriter(new FileWriter(new File(packageDirectory, className + ".java")));

		output.println("package " + packagePrefix + packageName + ";");

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
			processSimpleTypeAssignment(type);			
			berHelper.processNullTypeAssignment((NullType)type, className);
		}
		else  if (type.isBooleanType()) {
			processSimpleTypeAssignment(type);
			berHelper.processBooleanTypeAssignment((BooleanType)type, className);
		}
		else  if (type.isOctetStringType()) {
			processSimpleTypeAssignment(type);
			berHelper.processOctetStringTypeAssignment((OctetStringType)type, className);
		}	
		else  if (type.isRestrictedCharacterStringType()) {
			processSimpleTypeAssignment(type);
			berHelper.processRestrictedCharacterStringTypeAssignment((RestrictedCharacterStringType)type, className);
		}	
		else if (type.isSequenceType()) {
			processSequenceTypeAssignment((SequenceType)type, className);
			berHelper.processSequenceTypeAssignment((SequenceType)type, className);
		}
		else if (type.isChoiceType()) {
			processChoiceTypeAssignment((ChoiceType)type, className);
			berHelper.processChoiceTypeAssignment((ChoiceType)type, className);
		}
		else {
			throw new Exception("Code generation not supported for Type " + type.getName());
		}
	}
	
	private void processSimpleTypeAssignment(Type type) throws Exception {
		String javaType = Utils.mapToJava(type, false);
		output.println("private " + javaType + " value;");
		output.println("public " + javaType + " getValue() { return value; }");
		output.println("public void setValue(" + javaType + " value) { this.value = value; }");
	}
	
	private void processIntegerTypeAssignment(IntegerType integerType, String className) throws Exception {
		String javaType = Utils.mapToJava(integerType, false);

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
		
		String javaType = Utils.mapToJava(bitStringType, false);
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
			processSimpleNamedType(type, componentName, uComponentName);			
		}
		else  if (type.isBooleanType()) {
			processSimpleNamedType(type, componentName, uComponentName);
		}
		else  if (type.isOctetStringType()) {
			processSimpleNamedType(type, componentName, uComponentName);
		}	
		else  if (type.isRestrictedCharacterStringType()) {
			processSimpleNamedType(type, componentName, uComponentName);
		}	
		else  if (type.isTypeReference()) {
			processTypeReference((TypeReference)type, componentName, uComponentName);
		}	
		else 
			throw new Exception("Code generation not supported for component '" + componentName + "' of Type " + type.getName());


	}
	
	private void processSimpleNamedType(Type type, String componentName, String uComponentName) throws Exception {
		String javaType = Utils.mapToJava(type, false);

		output.println("private " + javaType + " " + componentName + ";");
		output.println("public " + javaType + " get" + uComponentName +"() { return " + componentName + "; }");
		output.println("public void set" + uComponentName + "(" + javaType + " " + componentName + ") { this." + componentName + " = " + componentName + "; }");
	}
	
	private void processIntegerNamedType(IntegerType integerType, String componentName, String uComponentName) throws Exception {
		String javaType = Utils.mapToJava(integerType, false);

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
	
	private void processBitStringNamedType(BitStringType bitStringType, String componentName, String uComponentName) throws Exception {
		String javaType = Utils.mapToJava(bitStringType, false);

		output.println("public static class " + uComponentName + "{");
		for(NamedNumber namedNumber : bitStringType.getNamedBitList()) {
			output.println("static final public int " + Utils.normalize(namedNumber.getName()) + "=" + namedNumber.getNumber() + ";");
		}
		output.println("}");
		
		output.println("private " + javaType + " " + componentName + ";");
		output.println("public " + javaType + " get" + uComponentName +"() { return " + componentName + "; }");
		output.println("public void set" + uComponentName + "(" + javaType + " " + componentName + ") { this." + componentName + " = " + componentName + "; }");
	}
	
	private void processTypeReference(TypeReference typeReference, String componentName, String uComponentName) throws Exception {
		String javaType = Utils.uNormalize(typeReference.getName());

		output.println("private " + javaType + " " + componentName + ";");
		output.println("public " + javaType + " get" + uComponentName +"() { return " + componentName + "; }");
		output.println("public void set" + uComponentName + "(" + javaType + " " + componentName + ") { this." + componentName + " = " + componentName + "; }");
	}
}
