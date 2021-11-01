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

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.yafred.asn1.model.Component;
import com.yafred.asn1.model.NamedNumber;
import com.yafred.asn1.model.Type;
import com.yafred.asn1.model.type.BitStringType;
import com.yafred.asn1.model.type.ChoiceType;
import com.yafred.asn1.model.type.EnumeratedType;
import com.yafred.asn1.model.type.IntegerType;
import com.yafred.asn1.model.type.ListOfType;
import com.yafred.asn1.model.type.NamedType;
import com.yafred.asn1.model.type.SequenceType;
import com.yafred.asn1.model.type.SetType;
import com.yafred.asn1.model.type.TypeReference;

public class ASNValueHelper {
	Generator generator;
	File packageDirectory;
	PrintWriter output;

	final static private String ASN_VALUE_READER = "com.yafred.asn1.runtime.ASNValueReader";
	final static private String ASN_VALUE_WRITER = "com.yafred.asn1.runtime.ASNValueWriter";

	
	ASNValueHelper(Generator generator) {
		this.generator = generator;
	}
	

	void processType(Type type, String className, boolean isComponent) throws Exception {
		this.output = generator.output; // for now, write encoding/decoding methods in the POJO class
		
        if(!isComponent) {
	        // readPdu method
			output.println("public static " + className + " readPdu(" + ASN_VALUE_READER
					+ " reader) throws Exception {");
	
			output.println(className + " ret = new " + className + "();");
			output.println("read(ret, reader);");			
			output.println("return ret;");			
			output.println("}");
	
			// writePdu method
			output.println("public static void writePdu(" + className + " pdu, "
					+ ASN_VALUE_WRITER + " writer) throws Exception {");
			output.println("write(pdu, writer);");
			output.println("}");
        }
        
        // switch
		if (type.isTypeReference()) {
			// nothing
		} 
		else if (!type.isTypeWithComponents() && !type.isListOfType()) {
	        // read method
			output.println("public static void read(" + className + " instance," + ASN_VALUE_READER +
	            " reader) throws Exception {");
			switchDecodeComponent(type, "value", className);
			output.println("return;");
			output.println("}");

			// write method
			output.println("public static void write(" + className + " instance," + ASN_VALUE_WRITER +
	            " writer) throws Exception {");
			switchEncodeComponent(type, "value", className);
			output.println("}");

		}
		else if (type.isSequenceType()) {
			processSequenceTypeAssignment((SequenceType)type, className);
		}
		else if (type.isSetType()) {
			processSetTypeAssignment((SetType)type, className);
		}
		else if (type.isListOfType()) {
			processListOfTypeAssignment((ListOfType)type, className);
		}
		else if (type.isChoiceType()) {
			processChoiceTypeAssignment((ChoiceType)type, className);
		}
		else {
			throw new Exception("ASNValueHelper.processType: Code generation not supported for Type " + type.getName());
		}	
	}
	
	
	private void processSequenceTypeAssignment(SequenceType sequenceType, String className) throws Exception {
		ArrayList<Component> componentList = new ArrayList<Component>();
		Utils.addAllIfNotNull(componentList, sequenceType.getRootComponentList());
		Utils.addAllIfNotNull(componentList, sequenceType.getExtensionComponentList());
		Utils.addAllIfNotNull(componentList, sequenceType.getAdditionalComponentList());
		
	    // write encoding code
		output.println("public static void write(" + className + " instance," + ASN_VALUE_WRITER +
	            " writer) throws Exception {");
		output.println("writer.beginSequence();");
		for(int componentIndex = 0; componentIndex < componentList.size(); componentIndex++) {
			Component component = componentList.get(componentIndex);
			if(!component.isNamedType()) throw new Exception("Component can only be a NamedType here");
			NamedType namedType = (NamedType)component;
			String componentName = Utils.normalize(namedType.getName());
			String componentGetter = "instance.get" + Utils.uNormalize(componentName) + "()";
			String componentClassName = Utils.uNormalize(namedType.getName());
			if(namedType.getType().isTypeReference()) {
				TypeReference typeReference = (TypeReference)namedType.getType();
				componentClassName = Utils.normalizeJavaType(typeReference, generator.options.getPackagePrefix());
			}
			output.println("if(" + componentGetter + "!=null){");
			output.println("writer.writeComponent(\"" + namedType.getName() + "\");");
			switchEncodeComponent(namedType.getType(), componentName, componentClassName);
			output.println("}");
		}
		output.println("writer.endSequence();");
		output.println("}");

        // write decoding code
		output.println("public static void read(" + className + " instance," + ASN_VALUE_READER +
	            " reader) throws Exception {");
		if(componentList.size() > 0) {
			output.println("String componentName = null;");
		}
		output.println("reader.readToken(); // read '{'");
		output.println("if(\"}\".equals(reader.lookAheadToken())) { // empty sequence");
		output.println("reader.readToken(); return;}");
	
		boolean lastIsOptional = true;
		for(int componentIndex = 0; componentIndex < componentList.size(); componentIndex++) {
			Component component = componentList.get(componentIndex);
			if(!component.isNamedType()) throw new Exception("Component can only be a NamedType here");
			NamedType namedType = (NamedType)component;
			String componentName = Utils.normalize(namedType.getName());
			String componentClassName = Utils.uNormalize(namedType.getName());
			if(namedType.getType().isTypeReference()) {
				TypeReference typeReference = (TypeReference)namedType.getType();
				componentClassName = Utils.normalizeJavaType(typeReference, generator.options.getPackagePrefix());
			}
			
			output.println("if(componentName == null) componentName = reader.readIdentifier();");
			output.println("if(componentName.equals(\"" + namedType.getName() + "\")) {");			
			switchDecodeComponent(namedType.getType(), componentName, componentClassName);
			output.println("if(\"}\".equals(reader.readToken())) { // read ',' or '}'");
			output.println("return;");
			output.println("}");
			output.println("componentName = null;");
			output.println("}");
			if(!namedType.isOptional()) {
				output.println("else { throw new Exception(\"Expecting " + namedType.getName() + " (not OPTIONAL)\"); }");
			}
			lastIsOptional = namedType.isOptional();
		}
		if (lastIsOptional && componentList.size() > 0) {
			output.println("if(componentName != null) throw  new Exception(\"Unexpected component \" + componentName);");
		}
		output.println("}");
	}

	
	private void processSetTypeAssignment(SetType setType, String className) throws Exception {
		ArrayList<Component> componentList = new ArrayList<Component>();
		Utils.addAllIfNotNull(componentList, setType.getRootComponentList());
		Utils.addAllIfNotNull(componentList, setType.getExtensionComponentList());
		Utils.addAllIfNotNull(componentList, setType.getAdditionalComponentList());
		
	    // write encoding code
		// Encoding section is equivalent to SEQUENCE encoding
		output.println("public static void write(" + className + " instance," + ASN_VALUE_WRITER +
	            " writer) throws Exception {");
		output.println("writer.beginSequence();");
		for(int componentIndex = 0; componentIndex < componentList.size(); componentIndex++) {
			Component component = componentList.get(componentIndex);
			if(!component.isNamedType()) throw new Exception("Component can only be a NamedType here");
			NamedType namedType = (NamedType)component;
			String componentName = Utils.normalize(namedType.getName());
			String componentGetter = "instance.get" + Utils.uNormalize(componentName) + "()";
			String componentClassName = Utils.uNormalize(namedType.getName());
			if(namedType.getType().isTypeReference()) {
				TypeReference typeReference = (TypeReference)namedType.getType();
				componentClassName = Utils.normalizeJavaType(typeReference, generator.options.getPackagePrefix());
			}
			output.println("if(" + componentGetter + "!=null){");
			output.println("writer.writeComponent(\"" + namedType.getName() + "\");");
			switchEncodeComponent(namedType.getType(), componentName, componentClassName);
			output.println("}");
		}
		output.println("writer.endSequence();");
		output.println("}");

        // write decoding code
		// decoding is different, we cannot rely on the order of components
		output.println("public static void read(" + className + " instance," + ASN_VALUE_READER +
	            " reader) throws Exception {");
		output.println("reader.readToken(); // read '{'");
		output.println("if(\"}\".equals(reader.lookAheadToken())) { // empty sequence");
		output.println("reader.readToken(); return;}");
		output.println("do {");
		output.println("String component = reader.readIdentifier();");
		output.println("switch(component) {");	
		for(int componentIndex = 0; componentIndex < componentList.size(); componentIndex++) {
			Component component = componentList.get(componentIndex);
			if(!component.isNamedType()) throw new Exception("Component can only be a NamedType here");
			NamedType namedType = (NamedType)component; 
			String componentName = Utils.normalize(namedType.getName());
			String componentClassName = Utils.uNormalize(namedType.getName());
			if(namedType.getType().isTypeReference()) {
				TypeReference typeReference = (TypeReference)namedType.getType();
				componentClassName = Utils.normalizeJavaType(typeReference, generator.options.getPackagePrefix());
			}
			output.println("case \"" + namedType.getName() + "\":");
			switchDecodeComponent(namedType.getType(), componentName, componentClassName);
			output.println("break;");

		}	
		output.println("default:");
		output.println("throw new Exception(\"Unexpected component: \" + component);");
		output.println("}");
		output.println("} while(\",\".equals(reader.readToken()));");
		output.println("}");
	}

	
	private void processListOfTypeAssignment(ListOfType listOfType, String className) throws Exception {
		Type elementType = listOfType.getElement().getType();
		String elementClassName = "";
		if(listOfType.getElement().getType().isTypeReference()) {
			elementClassName = Utils.normalizeJavaType((TypeReference)listOfType.getElement().getType(), generator.options.getPackagePrefix());
			elementType = ((TypeReference)listOfType.getElement().getType()).getBuiltinType();
		}
		else if(Utils.isConstructed(listOfType.getElement().getType())) {
			elementClassName = "Item";
			if(listOfType.getElement().getName() != null && !listOfType.getElement().getName().equals("")) {
				elementClassName = Utils.uNormalize(listOfType.getElement().getName());
			}
		}		

		String elementName = "";
		if(listOfType.getElement().getName() != null) {
			elementName = listOfType.getElement().getName();		
		}

	    // write encoding code
		output.println("public static void write(" + className + " instance," + ASN_VALUE_WRITER +
	            " writer) throws Exception {");
		output.println("writer.beginArray(\"" + elementName + "\");");
		output.println("if(instance.getValue() != null) {");
		output.println("for(int i=0; i<instance.getValue().size(); i++) {");
		
		switchEncodeListElement(elementType, elementClassName, "value");

		output.println("}");
		output.println("}");
		output.println("writer.endArray();");
		output.println("}");
		
        // write decoding code
		String javaType = elementClassName;
		
		if(!Utils.isConstructed(elementType)) {
			if(!elementType.isEnumeratedType()) {
				javaType= Utils.mapToJava(elementType);
			}
			else {
				if(elementClassName.equals("")) {
					javaType = "Enum";
				}
				else {
					javaType = elementClassName + ".Enum";
				}
			}
		}
		output.println("public static void read(" + className + " instance," + ASN_VALUE_READER +
	            " reader) throws Exception {");	
		output.println("instance.setValue(new java.util.ArrayList<" + javaType + ">());");
		output.println("reader.readToken(); // read '{'");		
		output.println("if(\"}\".equals(reader.lookAheadToken())) { // empty list");
		output.println("reader.readToken(); return;}");
		output.println("do {");
		if(!elementName.equals("")) {
			output.println("if(\"" + elementName + "\".equals(reader.lookAheadIdentifier())) {");			
			output.println("reader.readIdentifier(); }");			
		}
		switchDecodeListElement(elementType, elementClassName, "value", javaType);
		output.println("} while(\",\".equals(reader.readToken()));");
		output.println("}");
	}
	
	
	private void processChoiceTypeAssignment(ChoiceType choiceType, String className) throws Exception {
		ArrayList<Component> componentList = new ArrayList<Component>();
		Utils.addAllIfNotNull(componentList, choiceType.getRootAlternativeList());
		Utils.addAllIfNotNull(componentList, choiceType.getAdditionalAlternativeList());
		
	    // write encoding code
		output.println("public static void write(" + className + " instance," + ASN_VALUE_WRITER +
	            " writer) throws Exception {");
		for(int componentIndex = 0; componentIndex < componentList.size(); componentIndex++) {
			Component component = componentList.get(componentIndex);
			if(!component.isNamedType()) throw new Exception("Component can only be a NamedType here");
			NamedType namedType = (NamedType)component;
			String componentName = Utils.normalize(namedType.getName());
			String componentGetter = "instance.get" + Utils.uNormalize(componentName) + "()";
			String componentClassName = Utils.uNormalize(namedType.getName());
			if(namedType.getType().isTypeReference()) {
				TypeReference typeReference = (TypeReference)namedType.getType();
				componentClassName = Utils.normalizeJavaType(typeReference, generator.options.getPackagePrefix());
			}
			output.println("if(" + componentGetter + "!=null){");
			output.println("writer.writeSelection(\"" + namedType.getName() + "\");");
			switchEncodeComponent(namedType.getType(), componentName, componentClassName);
			output.println("}");
		}
		output.println("}");

        // write decoding code
		output.println("public static void read(" + className + " instance," + ASN_VALUE_READER +
	            " reader) throws Exception {");
		output.println("{");
		output.println("String alternative = reader.readIdentifier();");
		output.println("switch(alternative) {");
		
		for(int componentIndex = 0; componentIndex < componentList.size(); componentIndex++) {
			Component component = componentList.get(componentIndex);
			if(!component.isNamedType()) throw new Exception("Component can only be a NamedType here");
			NamedType namedType = (NamedType)component;
			String componentName = Utils.normalize(namedType.getName());
			String componentClassName = Utils.uNormalize(namedType.getName());
			if(namedType.getType().isTypeReference()) {
				TypeReference typeReference = (TypeReference)namedType.getType();
				componentClassName = Utils.normalizeJavaType(typeReference, generator.options.getPackagePrefix());
			}
			output.println("case \"" + namedType.getName() + "\":");
			output.println("if(\":\".equals(reader.lookAheadToken())) reader.readToken();"); // read optional ':'
			switchDecodeComponent(namedType.getType(), componentName, componentClassName);
			output.println("break;");
		}
		output.println("default:");
		output.println("throw new Exception(\"Unexpected alternative: \" + alternative);");
		output.println("}");
		output.println("}");
		output.println("}");
	}
	
	
	private void switchEncodeComponent(Type type, String componentName, String componentClassName) throws Exception {
		String referencedClassName = "";
		Type builtinType = type;
		if(builtinType.isTypeReference()) {
			referencedClassName = Utils.normalizeJavaType((TypeReference) builtinType, generator.options.getPackagePrefix());
			builtinType = ((TypeReference)builtinType).getBuiltinType();
		}
		
		String componentGetter = "instance.get" + Utils.uNormalize(componentName) + "()";
		
		if(builtinType.isRestrictedCharacterStringType()) {
			output.println("writer.writeRestrictedCharacterString(" +  componentGetter + ");");			
		}
		else if(builtinType.isIntegerType()) {
			IntegerType integerType = (IntegerType)builtinType;
			if (integerType.getNamedNumberList() != null) {
				output.println("{");
				output.println("switch(" +  componentGetter + ") {");
				for (NamedNumber namedNumber : integerType.getNamedNumberList()) {
					output.println("case " + namedNumber.getNumber() + ":");
					output.println("writer.writeIdentifier(\"" +  namedNumber.getName() + "\");");
					output.println("break;");
				}
				output.println("default:");
				output.println("writer.writeInteger(" +  componentGetter + ");");
				output.println("}");
				output.println("}");
			}
			else {
				output.println("writer.writeInteger(" +  componentGetter + ");");	
			}		
		}
		else if(builtinType.isBooleanType()) {
			output.println("writer.writeBoolean(" +  componentGetter + ");");			
		}	
		else if(builtinType.isBitStringType()) {
			BitStringType bitStringType = (BitStringType)builtinType;
			if(bitStringType.getNamedBitList() != null) {
				output.println("java.util.ArrayList<String> bitList = new java.util.ArrayList<String>();");
				output.println("int significantBitNumber=" +  componentGetter + ".length();");	
				output.println("for (int i = 0; i < significantBitNumber; i++) {");	
				output.println("if(" + componentGetter + ".get(i)) {");
				output.println("switch(i) {");
				for (NamedNumber namedNumber : bitStringType.getNamedBitList()) {
					output.println("case " + namedNumber.getNumber() + ":");
					output.println("bitList.add(\"" + namedNumber.getName() + "\");");
					output.println("break;");
				}
				output.println("default: // not in the list, give up");
				output.println("writer.writeBitString(" +  componentGetter + ");");	
				output.println("return;");
				output.println("}");
				output.println("}");
				output.println("}");
				output.println("writer.writeBitString(bitList);");						
			}
			else {
				output.println("writer.writeBitString(" +  componentGetter + ");");		
			}
		}
		else if(builtinType.isOctetStringType()) {
			output.println("writer.writeOctetString(" +  componentGetter + ");");			
		}
		else if(builtinType.isObjectIdentifierType()) {
			output.println("writer.writeObjectIdentifier(" +  componentGetter + ");");			
		}
		else if(builtinType.isRelativeOIDType()) {
			output.println("writer.writeRelativeOID(" +  componentGetter + ");");			
		}
		else if(builtinType.isNullType()) {
			output.println("writer.writeNull();");
		}
		else if(builtinType.isEnumeratedType()) {
			output.println("switch(" +  componentGetter + ") {");
			EnumeratedType enumeratedType = (EnumeratedType)builtinType;
			for(NamedNumber namedNumber : enumeratedType.getRootEnumeration()) {
				output.println("case " + Utils.normalizeConstant(namedNumber.getName()) + ":");
				output.println("writer.writeIdentifier(\"" +  namedNumber.getName() + "\");");
				output.println("break;");
			}
			if(enumeratedType.getAdditionalEnumeration() != null) {
				for(NamedNumber namedNumber : enumeratedType.getAdditionalEnumeration()) {
					output.println("case " + Utils.normalizeConstant(namedNumber.getName()) + ":");
					output.println("writer.writeIdentifier(\"" +  namedNumber.getName() + "\");");
					output.println("break;");
				}
			}
			output.println("}");
		}
		else if(type.isTypeReference()) {
			output.println(referencedClassName + ".write(" + componentGetter + ",writer);");		
		}
		else if(type.isTypeWithComponents()) {
			output.println(Utils.uNormalize(componentName) + ".write(" + componentGetter + ",writer);");		
		}
		else if(type.isListOfType()) {
			ListOfType listOfType = (ListOfType)type;
			Type elementType = listOfType.getElement().getType();
			String elementClassName = "";
			String elementName = "";
			if(listOfType.getElement().getType().isTypeReference()) {
				elementClassName = Utils.uNormalize(listOfType.getElement().getType().getName());
				elementType = ((TypeReference)listOfType.getElement().getType()).getBuiltinType();
			}
			else if(listOfType.getElement().getType().isTypeWithComponents() || listOfType.getElement().getType().isListOfType()) {
				elementClassName = "Item";
				if(listOfType.getElement().getName() != null && !listOfType.getElement().getName().equals("")) {
					elementClassName = Utils.uNormalize(listOfType.getElement().getName());
					elementName = listOfType.getElement().getName();
				}
			}
			output.println("writer.beginArray(\"" + elementName + "\");");
			output.println("if(" + componentGetter + " != null) {");
			output.println("for(int i=0;i<" + componentGetter + ".size(); i++) {");
			switchEncodeListElement(elementType, elementClassName, componentName);
			output.println("}");
			output.println("}");
			output.println("writer.endArray();");
		}
		else {
			throw new Exception("ASNValueHelper.switchEncodeComponent: Code generation not supported for Type " + type.getName());
		}
	}
	
	
	private void switchEncodeListElement(Type elementType, String elementClassName, String componentName) throws Exception {
		String componentGetter = "instance.get" + Utils.uNormalize(componentName) + "()";

		if(elementType.isRestrictedCharacterStringType()) {
			output.println("writer.writeRestrictedCharacterString(" + componentGetter + ".get(i));");			
		}
		else if(elementType.isIntegerType()) {
			IntegerType integerType = (IntegerType)elementType;
			if (integerType.getNamedNumberList() != null) {
				output.println("switch(" + componentGetter + ".get(i)) {");
				for (NamedNumber namedNumber : integerType.getNamedNumberList()) {
					output.println("case " + namedNumber.getNumber() + ":");
					output.println("writer.writeIdentifier(\"" +  namedNumber.getName() + "\");");		
					output.println("break;");
				}
				output.println("default:");
				output.println("writer.writeInteger(" + componentGetter + ".get(i));");			
				output.println("}");
			}
			else {
				output.println("writer.writeInteger(" + componentGetter + ".get(i));");			
			}
		}
		else if(elementType.isBooleanType()) {
			output.println("writer.writeBoolean(" + componentGetter + ".get(i));");			
		}
		else if(elementType.isBitStringType()) {
			BitStringType bitStringType = (BitStringType)elementType;
			if(bitStringType.getNamedBitList() != null) {
				output.println("java.util.ArrayList<String> bitList = new java.util.ArrayList<String>();");
				output.println("int significantBitNumber=" +  componentGetter + ".get(i).length();");	
				output.println("for (int k = 0; k < significantBitNumber; k++) {");	
				output.println("if(" + componentGetter + ".get(i).get(k)) {");
				output.println("switch(k) {");
				for (NamedNumber namedNumber : bitStringType.getNamedBitList()) {
					output.println("case " + namedNumber.getNumber() + ":");
					output.println("bitList.add(\"" + namedNumber.getName() + "\");");
					output.println("break;");
				}
				output.println("default: // not in the list, give up");
				output.println("bitList = null; // give up");	
				output.println("break;");
				output.println("}");
				output.println("}");
				output.println("}");
				output.println("if(bitList != null) {");
				output.println("writer.writeBitString(bitList);");						
				output.println("} else {");
				output.println("writer.writeBitString(" +  componentGetter + ".get(i));");		
				output.println("}");
			}
			else {
				output.println("writer.writeBitString(" +  componentGetter + ".get(i));");		
			}
		}
		else if(elementType.isOctetStringType()) {
			output.println("writer.writeOctetString(" + componentGetter + ".get(i));");			
		}
		else if(elementType.isObjectIdentifierType()) {
			output.println("writer.writeObjectIdentifier(" + componentGetter + ".get(i));");			
		}
		else if(elementType.isRelativeOIDType()) {
			output.println("writer.writeRelativeOID(" + componentGetter + ".get(i));");			
		}
		else if(elementType.isNullType()) {
			// do nothing
		}
		else if(elementType.isEnumeratedType()) {
			output.println("switch(" + componentGetter + ".get(i)) {");
			EnumeratedType enumeratedType = (EnumeratedType)elementType;
			for(NamedNumber namedNumber : enumeratedType.getRootEnumeration()) {
				output.println("case " + Utils.normalizeConstant(namedNumber.getName()) + ":");
				output.println("writer.writeIdentifier(\"" +  namedNumber.getName() + "\");");
				output.println("break;");
			}
			if(enumeratedType.getAdditionalEnumeration() != null) {
				for(NamedNumber namedNumber : enumeratedType.getAdditionalEnumeration()) {
					output.println("case " + Utils.normalizeConstant(namedNumber.getName()) + ":");
					output.println("writer.writeIdentifier(\"" +  namedNumber.getName() + "\");");
					output.println("break;");
				}
			}
			output.println("}");
		}
		else if(Utils.isConstructed(elementType)) {
			output.println(elementClassName + ".write(" + componentGetter + ".get(i),writer);");						
		}
		else {
			throw new Exception("ASNValueHelper.processListOfTypeAssignment: Code generation not supported for Type " + elementType.getName());
		}

	}
		
	
	private void switchDecodeComponent(Type type, String componentName, String componentClassName) throws Exception {
		
		Type builtinType = type;
		if(type.isTypeReference()) {
			builtinType = ((TypeReference)type).getBuiltinType();
		}
		
		String componentGetter = "instance.get" + Utils.uNormalize(componentName) + "()";
		String componentSetter = "instance.set" + Utils.uNormalize(componentName) + "(";
		
		if(builtinType.isRestrictedCharacterStringType()) {
			output.println(componentSetter + "reader.readRestrictedCharacterString());");
		}
		else if(builtinType.isIntegerType()) {
			IntegerType integerType = (IntegerType)builtinType;
			if (integerType.getNamedNumberList() != null) {
				output.println("{");
				output.println("String namedNumber = reader.readIdentifier();");
				output.println("switch(namedNumber) {");
				for (NamedNumber namedNumber : integerType.getNamedNumberList()) {
					output.println("case \"" + namedNumber.getName() + "\":");
					output.println(componentSetter + "Integer.valueOf(" + namedNumber.getNumber() + "));");
					output.println("break;");
				}
				output.println("default:");
				output.println(componentSetter + "Integer.valueOf(namedNumber));");
				output.println("}");
				output.println("}");
			}
			else {
				output.println(componentSetter + "reader.readInteger());");
			}
		}
		else if(builtinType.isBooleanType()) {
			output.println(componentSetter + "reader.readBoolean());");
		}	
		else if(builtinType.isBitStringType()) {
			boolean hasNamedBits = (((BitStringType)builtinType).getNamedBitList().size() != 0);
			if(hasNamedBits) {
				output.println("if(!\"{\".equals(reader.lookAheadToken())) {");
			}
			output.println(componentSetter + "reader.readBitString());");
			if(hasNamedBits) {
				output.println("return;");
				output.println("}");
				output.println(componentSetter + "new java.util.BitSet());");
				output.println("reader.readToken(); // read '{'");
				output.println("if(\"}\".equals(reader.lookAheadToken())) {");
				output.println("reader.readToken(); // read '}'");
				output.println("return; // empty list");
				output.println("}");
				output.println("do {");
				output.println("switch(reader.readIdentifier()) {");
				for(NamedNumber namedNumber : ((BitStringType)builtinType).getNamedBitList()) {
					output.println("case \"" + namedNumber.getName() + "\":");
					output.println(componentGetter + ".set(" + componentClassName + "." + Utils.normalizeConstant(namedNumber.getName()) + ");");
					output.println("break;");
				}
				output.println("}");
				output.println("} while(!\"}\".equals(reader.readToken()));");
			}
		}
		else if(builtinType.isOctetStringType()) {
			output.println(componentSetter + "reader.readOctetString());");
		}
		else if(builtinType.isObjectIdentifierType()) {
			output.println(componentSetter + "reader.readObjectIdentifier());");
		}
		else if(builtinType.isRelativeOIDType()) {
			output.println(componentSetter + "reader.readRelativeOID());");
		}
		else if(builtinType.isNullType()) {
			output.println(componentSetter + "reader.readNull());");
		}
		else if(builtinType.isEnumeratedType()) {
			EnumeratedType enumeratedType = (EnumeratedType)builtinType;
			String enumSuffix = "";
			if(type.isTypeReference() || componentName.equals("value")) {
				enumSuffix = ".Enum";
			}
			output.println("{");
			output.println("String identifier=reader.readIdentifier();");
			for(NamedNumber namedNumber : enumeratedType.getRootEnumeration()) {
				output.println("if(identifier.equals(\"" + namedNumber.getName() + "\")){");
				output.println(componentSetter + componentClassName + enumSuffix + "." + Utils.normalizeConstant(namedNumber.getName()) + ");");
				output.println("}");
			}
			if(enumeratedType.getAdditionalEnumeration() == null) {
				output.println("if(" + componentGetter + "==null){");
				output.println("throw new Exception(\"Invalid enumeration value: \" + identifier);");
				output.println("}");
			}
			else {
				for(NamedNumber namedNumber : enumeratedType.getAdditionalEnumeration()) {
					output.println("if(identifier.equals(\"" + namedNumber.getName() + "\")){");
					output.println(componentSetter + componentClassName + enumSuffix + "." + Utils.normalizeConstant(namedNumber.getName()) + ");");
					output.println("}");
				}
				output.println("// Extensible: this.getValue() can return null if unknown enum value is decoded.");
			}
			output.println("}");
		}
		else if(type.isTypeReference()) {
			output.println(componentSetter + "new " + componentClassName + "());");		
			output.println(componentClassName + ".read(" + componentGetter + ",reader);");		
		}
		else if(type.isTypeWithComponents()) {
			output.println(componentSetter + "new " + Utils.uNormalize(componentName) + "());");		
			output.println(Utils.uNormalize(componentName) + ".read(" + componentGetter + ",reader);");		
		}
		else if(type.isListOfType()) {
			ListOfType listOfType = (ListOfType)type;
			Type elementType = listOfType.getElement().getType();
			String elementClassName = "";
			if(listOfType.getElement().getType().isTypeReference()) {
				elementClassName = Utils.uNormalize(listOfType.getElement().getType().getName());
				elementType = ((TypeReference)listOfType.getElement().getType()).getBuiltinType();
			}
			else if(listOfType.getElement().getType().isTypeWithComponents() || listOfType.getElement().getType().isListOfType()) {
				elementClassName = "Item";
				if(listOfType.getElement().getName() != null && !listOfType.getElement().getName().equals("")) {
					elementClassName = Utils.uNormalize(listOfType.getElement().getName());
				}
			}
			String javaType = elementClassName;
			
			if(!Utils.isConstructed(elementType)) {
				if(!elementType.isEnumeratedType()) {
					javaType= Utils.mapToJava(elementType);
				}
				else {
					if(elementClassName.equals("")) {
						javaType = "Enum";
					}
					else {
						javaType = elementClassName + ".Enum";
					}
				}
			}
			
			String elementName = "";
			if(listOfType.getElement().getName() != null) {
				elementName = listOfType.getElement().getName();
			}
			
			output.println(componentSetter + "new java.util.ArrayList<" + javaType + ">());");
			
			output.println("reader.readToken(); // read '{'");		
			output.println("if(\"}\".equals(reader.lookAheadToken())) { // empty list");
			output.println("reader.readToken(); return;}");
			output.println("do {");
			if(!elementName.equals("")) {
				output.println("if(\"" + elementName + "\".equals(reader.lookAheadIdentifier())) {");			
				output.println("reader.readIdentifier(); }");			
			}
			switchDecodeListElement(elementType, elementClassName, componentName, javaType);
			output.println("} while(\",\".equals(reader.readToken()));");
		}
		else {
			throw new Exception("ASNValueHelp.switchDecodeComponent: Code generation not supported for Type " + type.getName());
		}
	}
	
	
	private void switchDecodeListElement(Type elementType, String elementClassName, String componentName, String javaType) throws Exception {
		String componentGetter = "instance.get" + Utils.uNormalize(componentName) + "()";

		if(elementType.isRestrictedCharacterStringType()) {
			output.println(componentGetter + ".add(reader.readRestrictedCharacterString());");	
		}
		else if(elementType.isIntegerType()) {
			IntegerType integerType = (IntegerType)elementType;
			if (integerType.getNamedNumberList() != null) {
				output.println("{");
				output.println("String namedNumber = reader.readIdentifier();");
				output.println("switch(namedNumber) {");
				for (NamedNumber namedNumber : integerType.getNamedNumberList()) {
					output.println("case \"" + namedNumber.getName() + "\":");
					output.println(componentGetter + ".add(Integer.valueOf(" + namedNumber.getNumber() + "));");
					output.println("break;");
				}
				output.println("default:");
				output.println(componentGetter + ".add(Integer.valueOf(namedNumber));");
				output.println("}");
				output.println("}");
			}
			else {
				output.println(componentGetter + ".add(reader.readInteger());");
			}
		}
		else if(elementType.isBooleanType()) {
			output.println(componentGetter + ".add(reader.readBoolean());");	
		}
		else if(elementType.isBitStringType()) {
			boolean hasNamedBits = (((BitStringType)elementType).getNamedBitList().size() != 0);
			if(hasNamedBits) {
				output.println("if(!\"{\".equals(reader.lookAheadToken())) {");
			}
			output.println(componentGetter + ".add(reader.readBitString());");	
			if(hasNamedBits) {
				output.println("} else {");
				output.println("java.util.BitSet item = new java.util.BitSet();");
				output.println(componentGetter + ".add(item);");
				output.println("reader.readToken(); // read '{'");
				output.println("if(\"}\".equals(reader.lookAheadToken())) {");
				output.println("reader.readToken(); // read '}'");
				output.println("return; // empty list");
				output.println("}");
				output.println("do {");
				output.println("switch(reader.readIdentifier()) {");
				for(NamedNumber namedNumber : ((BitStringType)elementType).getNamedBitList()) {
					output.println("case \"" + namedNumber.getName() + "\":");
					output.println("item.set(" + namedNumber.getNumber() + ");");
					output.println("break;");
				}
				output.println("}");
				output.println("} while(!\"}\".equals(reader.readToken()));");
				output.println("}");
			}
		}
		else if(elementType.isOctetStringType()) {
			output.println(componentGetter + ".add(reader.readOctetString());");	
		}
		else if(elementType.isObjectIdentifierType()) {
			output.println(componentGetter + ".add(reader.readObjectIdentifier());");	
		}
		else if(elementType.isRelativeOIDType()) {
			output.println(componentGetter + ".add(reader.readRelativeOID());");	
		}
		else if(elementType.isNullType()) {
			output.println(componentGetter + ".add(new Object());");	
		}
		else if(elementType.isEnumeratedType()) {
			EnumeratedType enumeratedType = (EnumeratedType)elementType;
			output.println("String identifier=reader.readIdentifier();");
			output.println(javaType + " item=null;");
			for(NamedNumber namedNumber : enumeratedType.getRootEnumeration()) {
				output.println("if(identifier.equals(\"" + namedNumber.getName() + "\")){");
				output.println("item=" + javaType + "." + Utils.normalizeConstant(namedNumber.getName()) + ";");
				output.println("}");
			}
			output.println("if(item!=null){");
			output.println(componentGetter + ".add(item);");
			output.println("}");
			if(enumeratedType.getAdditionalEnumeration() == null) {
				output.println("else {");
				output.println("throw new Exception(\"Invalid enumeration value: \" + identifier);");
				output.println("}");
			}
			else {
				for(NamedNumber namedNumber : enumeratedType.getAdditionalEnumeration()) {
					output.println("if(identifier.equals(\"" + namedNumber.getName() + "\")){");
					output.println("item=" + javaType + "." + Utils.normalizeConstant(namedNumber.getName()) + ";");
					output.println(componentGetter + ".add(item);");
					output.println("}");
				}
				output.println("// Extensible: instance.getValue() can return null if unknown enum value is decoded.");
			}
		}
		else if(Utils.isConstructed(elementType)) {
			output.println(componentGetter + ".add(new " + javaType + "());");						
			output.println(javaType + ".read(" +  componentGetter + ".get(" + componentGetter + ".size()-1),reader);");			
		}

	}	
}
