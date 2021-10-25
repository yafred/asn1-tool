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
package com.yafred.asn1.generator.go;


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

import com.yafred.asn1.model.Assignment;
import com.yafred.asn1.model.ModuleDefinition;
import com.yafred.asn1.model.NamedNumber;
import com.yafred.asn1.model.Specification;
import com.yafred.asn1.model.Type;
import com.yafred.asn1.model.TypeAssignment;
import com.yafred.asn1.model.type.BitStringType;
import com.yafred.asn1.model.type.EnumeratedType;
import com.yafred.asn1.model.type.IntegerType;
import com.yafred.asn1.model.type.TypeReference;
import com.yafred.asn1.parser.Asn1SpecificationWriter;

public class Generator {
	Options options;
	File outputDir;
	String packageName;
	File packageDirectory;
	PrintWriter output;

	boolean needsTypes = false;

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
		
		if(options.getPackagePrefix().length() > 0 && options.getPackagePrefix().substring(options.getPackagePrefix().length()-1) != "/") {
			options.setPackagePrefix(options.getPackagePrefix() + "/");
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
		// create package
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
		
		// Create a map file (ASN.1 references > Go types)
		PrintWriter fileWriter = new PrintWriter(new FileWriter(new File(outputDir, "asn1-go.map")));
		fileWriter.println("[");
		boolean isFirst = true;
		for (Entry<String, String> entry : typeMap) {
			if(isFirst) {
				isFirst = false;
			}
			else {
				fileWriter.println(",");
			}
		    fileWriter.println("{ \"asn1\": \"" + entry.getKey() + "\", \"go\": \"" + entry.getValue() + "\" }");
		}
		fileWriter.println("]");
		fileWriter.close();
	}
	
	
	private void processTypeAssignment(TypeAssignment typeAssignment, List<Map.Entry<String,String>> typeMap) throws Exception {
		// reset condition
		needsTypes = false;
		// get the ASN.1 spec for this type assignment
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Asn1SpecificationWriter asn1SpecificationWriter = new Asn1SpecificationWriter(new PrintStream(baos, true, "UTF-8"));
		asn1SpecificationWriter.visit(typeAssignment);
		String asn1Spec = new String(baos.toByteArray(), StandardCharsets.UTF_8);
		
		// systematically create a class
		String className = Utils.uNormalize(typeAssignment.getReference());

		StringWriter stringWriter = new StringWriter();
		output = new PrintWriter(stringWriter);

		Map.Entry<String,String> entry = new AbstractMap.SimpleEntry<String, String>(typeAssignment.getReference(), options.getPackagePrefix() + packageName + "." + className);
		typeMap.add(entry);

		if (typeAssignment.getType().isTypeReference()) {
			String parentClassName = Utils.uNormalize(((TypeReference) typeAssignment.getType()).getReferencedTypeName());
			output.println("type " + className + " " + parentClassName);
		} else {
			switchProcessTypeAssignment(typeAssignment.getType(), className);
		}

		// add encoding and decoding methods to the POJO
		this.addMethods(typeAssignment.getType(), className, false);
		output.close();
			
		PrintWriter fileWriter = new PrintWriter(new FileWriter(new File(packageDirectory, className + ".go")));

		fileWriter.println("/*");
		if(!options.getWatermark().equals("")) {
			fileWriter.println(options.getWatermark());
		}
		fileWriter.println(asn1Spec);
		fileWriter.println("*/");

		fileWriter.println("package " + options.getPackagePrefix() + packageName);
		fileWriter.println("import \"github.com/yafred/asn1-go/ber\""); 
		if(needsTypes) {
			fileWriter.println("import \"github.com/yafred/asn1-go/types\"");  
		}
		fileWriter.println("import \"errors\"");

		fileWriter.print(stringWriter.getBuffer().toString());
		fileWriter.close();
	}

	private void switchProcessTypeAssignment(Type type, String className) throws Exception {		
		if (type.isIntegerType()) {
			output.println("type " + className + " int");

			IntegerType integerType = (IntegerType)type;
			if (integerType.getNamedNumberList() != null) {
				processNamedNumberList(className, integerType.getNamedNumberList());
			}			
		}	
		
		if (type.isEnumeratedType()) {
			output.println("type " + className + " int");

			EnumeratedType enumeratedType = (EnumeratedType)type;
			if (enumeratedType.getRootEnumeration() != null) {
				processNamedNumberList(className, enumeratedType.getRootEnumeration());
			}
			
			if (enumeratedType.getAdditionalEnumeration() != null) {
				processNamedNumberList(className, enumeratedType.getAdditionalEnumeration());
			}		
		}	

		if (type.isBooleanType()) {
			output.println("type " + className + " bool");
		}

		if (type.isNullType()) {
			output.println("type " + className + " struct{}");
		}

		if (type.isOctetStringType()) {
			output.println("type " + className + " []byte");
		}

		if (type.isRestrictedCharacterStringType()) {
			output.println("type " + className + " string");
		}

		if (type.isBitStringType()) {
			output.println("type " + className + " struct{");
			output.println("bitString types.BitString");
			output.println("}");
			needsTypes = true;

			BitStringType bitStringType = (BitStringType)type;
			for(NamedNumber namedNumber : bitStringType.getNamedBitList()) {
				output.println("func (value *" + className + ") Set" + Utils.uNormalize(namedNumber.getName()) + "(bitValue bool) {");
				output.println("value.bitString.Set(" + namedNumber.getNumber() + ",bitValue)");
				output.println("}");	
				output.println("func (value *" + className + ") Get" + Utils.uNormalize(namedNumber.getName()) + "() (bool) {");
				output.println("return value.bitString.Get(" + namedNumber.getNumber() + ")");
				output.println("}");	
			}
		}
	}

	private void processNamedNumberList(String className, ArrayList<NamedNumber> namedNumberList) throws Exception {	
		for (NamedNumber namedNumber : namedNumberList) {
			output.println("func (value *" + className + ") Set" + Utils.uNormalize(namedNumber.getName()) + "() {");
			output.println("*value = " + namedNumber.getNumber());
			output.println("}");
			output.println("func (value *" + className + ") Is" + Utils.uNormalize(namedNumber.getName()) + "() (bool) {");
			output.println("if *value == " + namedNumber.getNumber() + "{");
			output.println("return true");
			output.println("} else {");
			output.println("return false");
			output.println("}");
			output.println("}");
		}			
	}
	

	private void addMethods(Type type, String className, boolean isInnerType) throws Exception {
		// add BER methods to the POJO
		berHelper.processType(type, className, isInnerType);
		// add ASN value methods to the POJO
		asnValueHelper.processType(type, className, isInnerType);		
		// add validation methods to the POJO
		validationHelper.processType(type, className, isInnerType);			
	}
}
