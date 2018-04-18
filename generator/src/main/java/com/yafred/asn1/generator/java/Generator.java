package com.yafred.asn1.generator.java;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.yafred.asn1.model.Assignment;
import com.yafred.asn1.model.BooleanType;
import com.yafred.asn1.model.IntegerType;
import com.yafred.asn1.model.ModuleDefinition;
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
			berHelper.processTypeAssignment(typeAssignment, className);
			output.println("}");
		} else {
			output.println("public class " + className);
			output.println("{");
			switchProcesstype(typeAssignment.getType(), className);
			berHelper.processTypeAssignment(typeAssignment, className);
			output.println("}");
		}

		output.close();
	}

	private void switchProcesstype(Type type, String className) throws Exception {		
		if (type.isIntegerType()) {
			processSimpleType(type);
			berHelper.processIntegerType((IntegerType)type, className);
		}
		else if (type.isNullType()) {
			processSimpleType(type);			
			berHelper.processNullType((NullType)type, className);
		}
		else  if (type.isBooleanType()) {
			processSimpleType(type);
			berHelper.processBooleanType((BooleanType)type, className);
		}
		else  if (type.isOctetStringType()) {
			processSimpleType(type);
			berHelper.processOctetStringType((OctetStringType)type, className);
		}	
		else  if (type.isRestrictedCharacterStringType()) {
			processSimpleType(type);
			berHelper.processRestrictedCharacterStringType((RestrictedCharacterStringType)type, className);
		}	
	}
	
	private void processSimpleType(Type type) throws Exception {
		String javaType = Utils.mapToJava(type, false);
		output.println("private " + javaType + " value;");
		output.println("public " + javaType + " getValue() { return value; }");
		output.println("public void setValue(" + javaType + " value) { this.value = value; }");
	}
}
