package com.yafred.asn1.generator.java;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.yafred.asn1.model.Assignment;
import com.yafred.asn1.model.BitStringType;
import com.yafred.asn1.model.BooleanType;
import com.yafred.asn1.model.EnumeratedType;
import com.yafred.asn1.model.IntegerType;
import com.yafred.asn1.model.ModuleDefinition;
import com.yafred.asn1.model.NamedNumber;
import com.yafred.asn1.model.NullType;
import com.yafred.asn1.model.OctetStringType;
import com.yafred.asn1.model.RestrictedCharacterStringType;
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

	public void process(Specification specification, Options options) throws Exception {
		processOptions(options);

		ArrayList<ModuleDefinition> moduleDefinitionList = specification.getModuleDefinitionList();

		for (ModuleDefinition moduleDefinition : moduleDefinitionList) {
			processModuleDefinition(moduleDefinition);
		}
	}

	private void processOptions(Options options) throws Exception {
		this.options = options;

		if ((options.getOutputDir() == null) || options.getOutputDir().equals("")) {
			throw new Exception("No output directory specified in options");
		}

		// check if output dir exis
		outputDir = new File(options.getOutputDir());

		if (!outputDir.exists()) {
			throw new Exception("Output directory does not exist: " + options.getOutputDir());
		}

		if (!outputDir.canWrite()) {
			throw new Exception("Cannot write output directory: " + options.getOutputDir());
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

		output.println("/**\n* Generating code for " + typeAssignment.getReference() + "\n*/");

		output.println("package " + packageName + ";");

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
		output.println("public enum " + className + "Enum {");
	
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
		
		output.println("private " + className + "Enum value;");
		output.println("public " + className + "Enum getValue() { return value; }");
		output.println("public void setValue(" + className + "Enum value) { this.value = value; }");
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
}
