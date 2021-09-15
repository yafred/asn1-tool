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

	
	public Generator() {
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
		// get the ASN.1 spec for this type assignment
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Asn1SpecificationWriter asn1SpecificationWriter = new Asn1SpecificationWriter(new PrintStream(baos, true, "UTF-8"));
		asn1SpecificationWriter.visit(typeAssignment);
		String asn1Spec = new String(baos.toByteArray(), StandardCharsets.UTF_8);
		
		// systematically create a class
		String className = Utils.uNormalize(typeAssignment.getReference());

		StringWriter stringWriter = new StringWriter();
		output = new PrintWriter(stringWriter);

		output.println("package " + options.getPackagePrefix() + packageName);
		
		Map.Entry<String,String> entry = new AbstractMap.SimpleEntry<String, String>(typeAssignment.getReference(), options.getPackagePrefix() + packageName + "." + className);
		typeMap.add(entry);


		output.close();
			
		PrintWriter fileWriter = new PrintWriter(new FileWriter(new File(packageDirectory, className + ".go")));
		fileWriter.println("/*");
		if(!options.getWatermark().equals("")) {
			fileWriter.println(options.getWatermark());
		}
		fileWriter.println(asn1Spec);
		fileWriter.println("*/");
		fileWriter.print(stringWriter.getBuffer().toString());
		fileWriter.close();
	}

	
}
