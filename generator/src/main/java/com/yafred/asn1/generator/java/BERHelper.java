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

import com.yafred.asn1.generator.common.TagHelper;
import com.yafred.asn1.model.Component;
import com.yafred.asn1.model.NamedNumber;
import com.yafred.asn1.model.Tag;
import com.yafred.asn1.model.Type;
import com.yafred.asn1.model.type.ChoiceType;
import com.yafred.asn1.model.type.EnumeratedType;
import com.yafred.asn1.model.type.ListOfType;
import com.yafred.asn1.model.type.NamedType;
import com.yafred.asn1.model.type.SequenceType;
import com.yafred.asn1.model.type.SetType;
import com.yafred.asn1.model.type.TypeReference;

public class BERHelper {
	Generator generator;
	File packageDirectory;
	PrintWriter output;

	final static private String BER_READER = "com.yafred.asn1.runtime.BERReader";
	final static private String BER_WRITER = "com.yafred.asn1.runtime.BERWriter";

	
	BERHelper(Generator generator) {
		this.generator = generator;
	}
	

	void processType(Type type, String className, boolean isInnerType) throws Exception {
		this.output = generator.output; // for now, write encoding/decoding methods in the POJO class
		
        ArrayList<Tag> tagList = Utils.getTagChain(type);
		
        if(!isInnerType) {
	        // readPdu method
			output.println("public static " + className + " readPdu(" + BER_READER
					+ " reader) throws Exception {");
			writePduTagsDecode(type);
	
			if (type.isChoiceType() || (type.isTypeReference() && ((TypeReference)type).getBuiltinType().isChoiceType())) { 
				output.println("reader.readTag();");
				output.println("reader.readLength();");
				output.println("reader.setTagMatched(false);");
			}
	
			output.println(className + " ret = new " + className + "();");
			output.println("read(ret, reader, reader.getLengthValue());");			
			output.println("return ret;");			
			output.println("}");
	
			// writePdu method
			output.println("public static void writePdu(" + className + " pdu, "
					+ BER_WRITER + " writer) throws Exception {");
	        String lengthDeclaration = "";
	        if (tagList != null && tagList.size() != 0) { // it is not an untagged CHOICE
	            lengthDeclaration = "int componentLength = ";
	        }
	        output.println(lengthDeclaration + "write(pdu, writer);");
			writeTagsEncode(type);
			output.println("writer.flush();");
			output.println("}");
        }
					
		// switch
		if (type.isTypeReference()) {
			// nothing more
		} 
		else if (!type.isTypeWithComponents() && !type.isListOfType()) {
	        // read method
			output.println("public static void read(" + className + " instance," + BER_READER +
	            " reader, int componentLength) throws Exception {");
			switchDecodeComponent(type, "value", className);
			output.println("return;");
			output.println("}");

			// write method
			output.println("public static int write(" + className + " instance," + BER_WRITER +
	            " writer) throws Exception {");
			output.println("int componentLength=0;");
			switchEncodeComponent(type, "value", className);
			output.println("return componentLength;");
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
			throw new Exception("BERHelp.processType: Code generation not supported for Type " + type.getName());
		}
	}
	
		
	private void writeTagsEncode(Type type) throws Exception {
		writeTagsEncode(type, null);
	}
	
	
	private void writeTagsEncode(Type type, Tag automaticTag) throws Exception {
		ArrayList<Tag> tagList = Utils.getTagChain(type);
		
		if(tagList == null) {
			tagList = new ArrayList<Tag>();
		}
		if (tagList.size() == 0 && automaticTag == null) { // it is a untagged CHOICE, no tag to write
			return;
		}
		
		if(automaticTag != null) {
			if(tagList.size() == 0) {
				tagList.add(automaticTag);
			}
			else {
				tagList.set(0, automaticTag);
			}
		}
		for (int iTag = tagList.size() - 1; iTag >= 0; iTag--) {
			boolean isConstructedForm = true;

			if ((iTag == (tagList.size() - 1)) && !Utils.isConstructed(type)) {
				isConstructedForm = false;
			}

			output.println("componentLength += writer.writeLength(componentLength);");

			TagHelper tagHelper = new TagHelper(tagList.get(iTag), !isConstructedForm);
		
			output.println(
						"componentLength += writer.writeOctetString(new byte[] {" + tagHelper.tagBytesAsString() + "}); /* " + tagHelper.toString() + " */");
		}
	}


	/*
	 * Returns false if there is no tag (untagged CHOICE)
	 */
	private boolean writeSequenceTagsDecode(NamedType namedType, Tag automaticTag) throws Exception {
		
		if(namedType.isOptional()) {
			// potential end of list (indefinite length form)
			output.println("if(length==-1 && reader.matchTag(new byte[]{0})) {");
			output.println("reader.mustReadZeroLength();");
			output.println("return;");
			output.println("}");
		}

		ArrayList<Tag> tagList = Utils.getTagChain(namedType.getType());
		
		if(tagList == null) {
			tagList = new ArrayList<Tag>();
		}
		if (tagList.size() == 0 && automaticTag == null) { // it is a untagged CHOICE, no tag to write
			return false;
		}
		
		if(automaticTag != null) {
			if(tagList.size() == 0) {
				tagList.add(automaticTag);
			}
			else {
				tagList.set(0, automaticTag);
			}
		}

		for (int iTag = 0; iTag < tagList.size(); iTag++) {
			boolean isConstructedForm = true;

			if ((iTag == (tagList.size() - 1)) && !Utils.isConstructed(namedType.getType())) {
				isConstructedForm = false;
			}

			TagHelper tagHelper = new TagHelper(tagList.get(iTag), !isConstructedForm);
			
			if(iTag == 0) {
				if(namedType.isOptional()) {
					output.println("reader.matchTag(new byte[] {" + tagHelper.tagBytesAsString() + "}); /* " + tagHelper.toString() + " */");
					output.println("if(reader.isTagMatched()){");
					output.println("reader.readLength();");
					output.println("if(length!=-1) length-=reader.getLengthLength();");
					output.println("}");
				}
				else {
					output.println("reader.mustMatchTag(new byte[] {" + tagHelper.tagBytesAsString() + "}); /* " + tagHelper.toString() + " */");					
					output.println("reader.readLength();");
					output.println("if(length!=-1) length-=reader.getLengthLength();");
				}
			}
			else {
				if(namedType.isOptional()) {
					output.println("if(reader.isTagMatched()){");
				}
				output.println("reader.readTag();");
				output.println("if(length!=-1) length-=reader.getTagLength();");
				output.println("reader.mustMatchTag(new byte[] {" + tagHelper.tagBytesAsString() + "}); /* " + tagHelper.toString() + " */");
				output.println("reader.readLength();");
				output.println("if(length!=-1) length-=reader.getLengthLength();");
				if(namedType.isOptional()) {
					output.println("}");
				}
			}
		}
		
		return true;
	}

	
	private void writeUntaggedChoiceTagsLookAhead(Type type) throws Exception {
		ChoiceType choiceType = null;
		/* It can only be a CHOICE or a ref to a CHOICE. Otherwise, fire a CastClassException */
		if(type.isTypeReference()) {
			choiceType = (ChoiceType)((TypeReference)type).getBuiltinType();
		}
		else {
			choiceType = (ChoiceType)type;
		}
		
		ArrayList<NamedType> alternativeListIncludingNested = choiceType.getAlternativeListIncludingNested();
		if(alternativeListIncludingNested == null)  {
			return;
		}
		
		output.println("if(reader.lookAheadTag(new byte[][] {");
		
		int iAlternative = 0;
		for(NamedType alternative : alternativeListIncludingNested) {
			ArrayList<Tag> tagList = Utils.getTagChain(alternative.getType());
			for (int iTag = 0; iTag < tagList.size(); iTag++) {
				boolean isConstructedForm = true;

				if ((iTag == (tagList.size() - 1)) && !Utils.isConstructed(alternative.getType())) {
					isConstructedForm = false;
				}

				TagHelper tagHelper = new TagHelper(tagList.get(iTag), !isConstructedForm);

				if(iAlternative++ != 0) {
					output.print(",");
				}
				output.println("new byte[] {" + tagHelper.tagBytesAsString() + "}");
			}
		}
		output.println("}))");
	}
	
	
	/*
	 * Returns false if there is no tag (untagged CHOICE)
	 */
	private boolean writeSetTagsDecode(NamedType namedType, Tag automaticTag) throws Exception {
		
		// potential end of list (indefinite length form)
		output.println("if(length==-1 && reader.matchTag(new byte[]{0})) {");
		output.println("reader.mustReadZeroLength();");
		output.println("return;");
		output.println("}");

		ArrayList<Tag> tagList = Utils.getTagChain(namedType.getType());

		if(tagList == null) {
			tagList = new ArrayList<Tag>();
		}
		if (tagList.size() == 0 && automaticTag == null) { // it is a untagged CHOICE
			return false;
		}
		
		if(automaticTag != null) {
			if(tagList.size() == 0) {
				tagList.add(automaticTag);
			}
			else {
				tagList.set(0, automaticTag);
			}
		}

		for (int iTag = 0; iTag < tagList.size(); iTag++) {
			boolean isConstructedForm = true;

			if ((iTag == (tagList.size() - 1)) && !Utils.isConstructed(namedType.getType())) {
				isConstructedForm = false;
			}

			TagHelper tagHelper = new TagHelper(tagList.get(iTag), !isConstructedForm);
			
			if(iTag == 0) {
				output.println("reader.matchTag(new byte[] {" + tagHelper.tagBytesAsString() + "}); /* " + tagHelper.toString() + " */");
				output.println("if(reader.isTagMatched()){");
				output.println("reader.readLength();");
				output.println("if(length!=-1) length-=reader.getLengthLength();");
				output.println("}");
			}
			else {
				output.println("if(reader.isTagMatched()){");
				output.println("reader.readTag();");
				output.println("if(length!=-1) length-=reader.getTagLength();");
				output.println("reader.mustMatchTag(new byte[] {" + tagHelper.tagBytesAsString() + "}); /* " + tagHelper.toString() + " */");
				output.println("reader.readLength();");
				output.println("if(length!=-1) length-=reader.getLengthLength();");
				output.println("}");
			}
		}
		
		return true;
	}

	
	/*
	 * Returns false if there is no tag (untagged CHOICE)
	 */
	private boolean writeChoiceTagsDecode(NamedType namedType, Tag automaticTag) throws Exception {
		ArrayList<Tag> tagList = Utils.getTagChain(namedType.getType());

		if(tagList == null) {
			tagList = new ArrayList<Tag>();
		}
		if (tagList.size() == 0 && automaticTag == null) { // it is a untagged CHOICE
			return false;
		}
		
		if(automaticTag != null) {
			if(tagList.size() == 0) {
				tagList.add(automaticTag);
			}
			else {
				tagList.set(0, automaticTag);
			}
		}

		for (int iTag = 0; iTag < tagList.size(); iTag++) {
			boolean isConstructedForm = true;

			if ((iTag == (tagList.size() - 1)) && !Utils.isConstructed(namedType.getType())) {
				isConstructedForm = false;
			}

			TagHelper tagHelper = new TagHelper(tagList.get(iTag), !isConstructedForm);
			
			if(iTag == 0) {
				output.println("reader.matchTag(new byte[] {" + tagHelper.tagBytesAsString() + "}); /* " + tagHelper.toString() + " */");
			}
			else {
				output.println("if(reader.isTagMatched()){");
				output.println("reader.readTag();");
				output.println("componentLength-=reader.getTagLength();");
				output.println("reader.mustMatchTag(new byte[] {" + tagHelper.tagBytesAsString() + "}); /* " + tagHelper.toString() + " */");
				output.println("reader.readLength();");
				output.println("componentLength-=reader.getLengthLength();");
				output.println("}");
			}
		}
		
		return true;
	}

	
	private boolean writeElementTagsDecode(Type type) throws Exception {
		
		// potential end of list (indefinite length form)
		output.println("if(listLength==-1 && reader.matchTag(new byte[]{0})) {");
		output.println("reader.mustReadZeroLength();");
		output.println("break;");
		output.println("}");

		ArrayList<Tag> tagList = Utils.getTagChain(type);
		if (tagList == null || tagList.size() == 0) { // it is a untagged CHOICE, no tag to write
			return false;
		}

		for (int iTag = 0; iTag < tagList.size(); iTag++) {
			boolean isConstructedForm = true;

			if ((iTag == (tagList.size() - 1)) && !Utils.isConstructed(type)) {
				isConstructedForm = false;
			}

			TagHelper tagHelper = new TagHelper(tagList.get(iTag), !isConstructedForm);
			
			if(iTag == 0) {
				output.println("reader.mustMatchTag(new byte[] {" + tagHelper.tagBytesAsString() + "}); /* " + tagHelper.toString() + " */");
				output.println("reader.readLength();");
				output.println("if(listLength!=-1) listLength-=reader.getLengthLength();");
			}
			else {
				output.println("reader.readTag();");
				output.println("if(listLength!=-1) listLength-=reader.getTagLength();");
				output.println("reader.mustMatchTag(new byte[] {" + tagHelper.tagBytesAsString() + "}); /* " + tagHelper.toString() + " */");
				output.println("reader.readLength();");
				output.println("if(listLength!=-1) listLength-=reader.getLengthLength();");
			}
		}
		return true;
	}

	
	private void writePduTagsDecode(Type type) throws Exception {
		ArrayList<Tag> tagList = Utils.getTagChain(type);
		if (tagList != null && tagList.size() != 0) { // it is not an untagged CHOICE
			for (int iTag = 0; iTag < tagList.size(); iTag++) {
				boolean isConstructedForm = true;

				if ((iTag == (tagList.size() - 1)) && !Utils.isConstructed(type)) {
					isConstructedForm = false;
				}

				TagHelper tagHelper = new TagHelper(tagList.get(iTag), !isConstructedForm);

				output.println("reader.readTag();");
				output.println(
						"reader.mustMatchTag(new byte[] {" + tagHelper.tagBytesAsString() + "}); /* " + tagHelper.toString() + " */");
				
				output.println("reader.readLength();");
			}
		}
	}
	
	
	private void processSequenceTypeAssignment(SequenceType sequenceType, String className) throws Exception {
		ArrayList<Component> componentList = new ArrayList<Component>();
		Utils.addAllIfNotNull(componentList, sequenceType.getRootComponentList());
		Utils.addAllIfNotNull(componentList, sequenceType.getExtensionComponentList());
		Utils.addAllIfNotNull(componentList, sequenceType.getAdditionalComponentList());
		
	    // write encoding code
		output.println("public static int write(" + className + " instance," + BER_WRITER +
	            " writer) throws Exception {");
		output.println("int length=0;");
		for(int componentIndex = componentList.size()-1; componentIndex >= 0; componentIndex--) {
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
			output.println("int componentLength=0;");
			switchEncodeComponent(namedType.getType(), componentName, componentClassName);
			Tag automaticTag = null;
			if(sequenceType.isAutomaticTaggingSelected()) {
				automaticTag = new Tag(Integer.valueOf(componentIndex), null, null);
			}
			writeTagsEncode(namedType.getType(), automaticTag);
			output.println("length+=componentLength;");
			output.println("}");
		}
		output.println("return length;");
		output.println("}");

        // write decoding code
		output.println("public static void read(" + className + " instance," + BER_READER +
	            " reader, int length) throws Exception {");
		if(componentList.size() > 0) {
			output.println("int componentLength=0;");
		}
		for(int componentIndex = 0; componentIndex < componentList.size(); componentIndex++) {
			output.println("if(length==0) return;"); 			
			if(componentIndex != 0) {
				output.println("if(reader.isTagMatched()){");
			}
			output.println("reader.readTag();");
			output.println("if(length!=-1) length-=reader.getTagLength();");
			if(componentIndex != 0) {
				output.println("}");
			}

			Component component = componentList.get(componentIndex);
			if(!component.isNamedType()) throw new Exception("Component can only be a NamedType here");
			NamedType namedType = (NamedType)component;
			String componentName = Utils.normalize(namedType.getName());
			String componentClassName = Utils.uNormalize(namedType.getName());
			if(namedType.getType().isTypeReference()) {
				TypeReference typeReference = (TypeReference)namedType.getType();
				componentClassName = Utils.normalizeJavaType(typeReference, generator.options.getPackagePrefix());
			}
			Tag automaticTag = null;
			if(sequenceType.isAutomaticTaggingSelected()) {
				automaticTag = new Tag(Integer.valueOf(componentIndex), null, null);
			}
			
			boolean hasTags = writeSequenceTagsDecode(namedType, automaticTag);
			if(hasTags) {
				if(namedType.isOptional()) {
					output.println("if(reader.isTagMatched()){");
				}
				output.println("componentLength=reader.getLengthValue();");
				if(namedType.getType().isChoiceType() || (namedType.getType().isTypeReference() && ((TypeReference)namedType.getType()).getBuiltinType().isChoiceType())) {
					output.println("reader.readTag();");
					output.println("if (length != -1) length -= reader.getTagLength();");
					output.println("reader.readLength();");
					output.println("if (length != -1) length -= reader.getLengthLength();");
					output.println("reader.setTagMatched(false);");
					output.println("componentLength=reader.getLengthValue();");
				}
				switchDecodeComponent(namedType.getType(), componentName, componentClassName);
				output.println("if(length!=-1) length-=componentLength;");
				if(namedType.isOptional()) {
					output.println("}");
				}
			}
			else {
				output.println("// component is an untagged CHOICE");	
				writeUntaggedChoiceTagsLookAhead(namedType.getType());
				output.println("{");
				output.println("reader.readLength();");
				output.println("if (length != -1) length -= reader.getLengthLength();");
				output.println("componentLength=reader.getLengthValue();");
				output.println("reader.setTagMatched(false);");
				switchDecodeComponent(namedType.getType(), componentName, componentClassName);	
				output.println("if(length!=-1) length-=componentLength;");
				output.println("}");
				if(!namedType.isOptional()) {
					output.println("// we need to validate " + componentName + " is not null");
				}
			}	
		}
		
		output.println("if(length==-1) {");
		output.println("reader.readTag();");
		output.println("reader.mustMatchTag(new byte[]{0});");
		output.println("reader.mustReadZeroLength();");
		output.println("}");

		output.println("else if(length!=0) throw new Exception(\"length should be 0, not \" + length);"); 
		output.println("return;");
		output.println("}");
	}

	
	private void processSetTypeAssignment(SetType setType, String className) throws Exception {
		ArrayList<Component> componentList = new ArrayList<Component>();
		Utils.addAllIfNotNull(componentList, setType.getRootComponentList());
		Utils.addAllIfNotNull(componentList, setType.getExtensionComponentList());
		Utils.addAllIfNotNull(componentList, setType.getAdditionalComponentList());
		
	    // write encoding code
		// Encoding section is equivalent to SEQUENCE encoding
		output.println("public static int write(" + className + " instance," + BER_WRITER +
	            " writer) throws Exception {");
		output.println("int length=0;");
		for(int componentIndex = componentList.size()-1; componentIndex >= 0; componentIndex--) {
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
			output.println("int componentLength=0;");
			switchEncodeComponent(namedType.getType(), componentName, componentClassName);
			Tag automaticTag = null;
			if(setType.isAutomaticTaggingSelected()) {
				automaticTag = new Tag(Integer.valueOf(componentIndex), null, null);
			}
			writeTagsEncode(namedType.getType(), automaticTag);
			output.println("length+=componentLength;");
			output.println("}");
		}
		output.println("return length;");
		output.println("}");

        // write decoding code
		// decoding is different, we cannot rely on the order of components
		output.println("public static void read(" + className + " instance," + BER_READER +
	            " reader, int length) throws Exception {");

		output.println("while(length==-1||length>0){");

		if(componentList.size() > 0) {
			output.println("int componentLength=0;");
		}

		output.println("reader.readTag();");
		output.println("if(length!=-1) length-=reader.getTagLength();");
		output.println("if(length==-1 && reader.matchTag(new byte[]{0})) {");
		output.println("reader.mustReadZeroLength();");
		output.println("return;");
		output.println("}");

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
			Tag automaticTag = null;
			if(setType.isAutomaticTaggingSelected()) {
				automaticTag = new Tag(Integer.valueOf(componentIndex), null, null);
			}
			
			boolean hasTags = writeSetTagsDecode(namedType, automaticTag);
			if(hasTags) {
				output.println("if(reader.isTagMatched()){");
				output.println("componentLength=reader.getLengthValue();");
				switchDecodeComponent(namedType.getType(), componentName, componentClassName);
				output.println("if(length!=-1) length-=componentLength;");
				output.println("continue;");
				output.println("}");
			}
			else {
				output.println("// component is an untagged CHOICE");	
				writeUntaggedChoiceTagsLookAhead(namedType.getType());
				output.println("{");
				output.println("reader.readLength();");
				output.println("if (length != -1) length -= reader.getLengthLength();");
				output.println("componentLength=reader.getLengthValue();");
				output.println("reader.setTagMatched(false);");
				switchDecodeComponent(namedType.getType(), componentName, componentClassName);	
				output.println("if(length!=-1) length-=componentLength;");
				output.println("continue;");
				output.println("}");
			}

			output.println();
		}	
		
		output.println("}");
		output.println("if(length!=-1 && length!=0) throw new Exception(\"length should be 0, not \" + length);"); 

		output.println("return;");
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
		
	    // write encoding code
		output.println("public static int write(" + className + " instance," + BER_WRITER +
	            " writer) throws Exception {");
		output.println("int length=0;");
		output.println("if(instance.getValue() != null) {");
		output.println("for(int i=instance.getValue().size()-1; i>=0; i--) {");
		output.println("int componentLength=0;");
		
		switchEncodeListElement(elementType, elementClassName, "value");

		writeTagsEncode(listOfType.getElement().getType());
		output.println("length+=componentLength;");
		output.println("}");
		output.println("}");
		output.println("return length;");
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
		output.println("public static void read(" + className + " instance," + BER_READER +
	            " reader, int listLength) throws Exception {");
		output.println("instance.setValue(new java.util.ArrayList<" + javaType + ">());");
		output.println("while(listLength > 0 || listLength==-1) {");
		output.println("reader.readTag();");
		output.println("if(listLength!=-1) listLength-=reader.getTagLength();");
		boolean hasTags = writeElementTagsDecode(listOfType.getElement().getType());
		if(!hasTags) {
			output.println("// list element is an untagged CHOICE");
			output.println("reader.readLength();");
			output.println("if(listLength!=-1) listLength-=reader.getLengthLength();");

		}
		output.println("int componentLength=reader.getLengthValue();");
		
		switchDecodeListElement(elementType, elementClassName, "value", javaType);
		
		output.println("if(listLength!=-1) listLength-=componentLength;");
		output.println("}");
		output.println("return;");
		output.println("}");
	}
	
	
	private void processChoiceTypeAssignment(ChoiceType choiceType, String className) throws Exception {
		ArrayList<Component> componentList = new ArrayList<Component>();
		Utils.addAllIfNotNull(componentList, choiceType.getRootAlternativeList());
		Utils.addAllIfNotNull(componentList, choiceType.getAdditionalAlternativeList());
		
	    // write encoding code
		output.println("public static int write(" + className + " instance," + BER_WRITER +
	            " writer) throws Exception {");
		for(int componentIndex = componentList.size()-1; componentIndex >= 0; componentIndex--) {
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
			output.println("int componentLength=0;");
			switchEncodeComponent(namedType.getType(), componentName, componentClassName);
			Tag automaticTag = null;
			if(choiceType.isAutomaticTaggingSelected()) {
				automaticTag = new Tag(Integer.valueOf(componentIndex), null, null);
			}
			writeTagsEncode(namedType.getType(), automaticTag);
			output.println("return componentLength;");
			output.println("}");
		}
		output.println("return 0;");
		output.println("}");

        // write decoding code
		output.println("public static void read(" + className + " instance," + BER_READER +
	            " reader, int componentLength) throws Exception {");
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
			Tag automaticTag = null;
			if(choiceType.isAutomaticTaggingSelected()) {
				automaticTag = new Tag(Integer.valueOf(componentIndex), null, null);
			}

			boolean hasTags = writeChoiceTagsDecode(namedType, automaticTag);
			if(hasTags) {
				output.println("if(reader.isTagMatched()){");
				if(namedType.getType().isChoiceType() || (namedType.getType().isTypeReference() && ((TypeReference)namedType.getType()).getBuiltinType().isChoiceType())) {
					output.println("reader.readTag();");
					output.println("reader.readLength();");
					output.println("reader.setTagMatched(false);");
					output.println("componentLength=reader.getLengthValue();");
				}
				switchDecodeComponent(namedType.getType(), componentName, componentClassName);
				output.println("return;");
				output.println("}");
			}
			else {
				output.println("// component is an untagged CHOICE. See if we have to enter it.");
				writeUntaggedChoiceTagsLookAhead(namedType.getType());
				output.println("{");
				switchDecodeComponent(namedType.getType(), componentName, componentClassName);	
				output.println("return;");				
				output.println("}");
			}

			output.println();
		}
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
			output.println("componentLength=writer.writeRestrictedCharacterString(" +  componentGetter + ");");			
		}
		else if(builtinType.isIntegerType()) {
			output.println("componentLength=writer.writeInteger(" +  componentGetter + ");");			
		}
		else if(builtinType.isBooleanType()) {
			output.println("componentLength=writer.writeBoolean(" +  componentGetter + ");");			
		}	
		else if(builtinType.isBitStringType()) {
			output.println("componentLength=writer.writeBitString(" +  componentGetter + ");");			
		}
		else if(builtinType.isOctetStringType()) {
			output.println("componentLength=writer.writeOctetString(" +  componentGetter + ");");			
		}
		else if(builtinType.isObjectIdentifierType()) {
			output.println("componentLength=writer.writeObjectIdentifier(" +  componentGetter + ");");			
		}
		else if(builtinType.isRelativeOIDType()) {
			output.println("componentLength=writer.writeRelativeOID(" +  componentGetter + ");");			
		}
		else if(builtinType.isNullType()) {
			// do nothing
		}
		else if(builtinType.isEnumeratedType()) {
			output.println("int intValue=-1;");
			output.println("switch(" +  componentGetter + ") {");
			EnumeratedType enumeratedType = (EnumeratedType)builtinType;
			for(NamedNumber namedNumber : enumeratedType.getRootEnumeration()) {
				output.println("case " + Utils.normalizeConstant(namedNumber.getName()) + ":");
				output.println("intValue=" + namedNumber.getNumber() + ";");
				output.println("break;");
			}
			if(enumeratedType.getAdditionalEnumeration() != null) {
				for(NamedNumber namedNumber : enumeratedType.getAdditionalEnumeration()) {
					output.println("case " + Utils.normalizeConstant(namedNumber.getName()) + ":");
					output.println("intValue=" + namedNumber.getNumber() + ";");
					output.println("break;");
				}
			}
			output.println("}");
			output.println("componentLength=writer.writeInteger(intValue);");			
		}
		else if(type.isTypeReference()) {
			output.println("componentLength=" + referencedClassName + ".write(" + componentGetter + ",writer);");		
		}
		else if(type.isTypeWithComponents()) {
			output.println("componentLength=" + Utils.uNormalize(componentName) + ".write(" + componentGetter + ",writer);");		
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
			output.println("int listLength=0;");
			output.println("if(" + componentGetter + " != null) {");
			output.println("for(int i=" + componentGetter + ".size()-1; i>=0; i--) {");
			output.println("componentLength=0;");
			switchEncodeListElement(elementType, elementClassName, componentName);
			writeTagsEncode(listOfType.getElement().getType());
			output.println("listLength+=componentLength;");
			output.println("}");
			output.println("}");
			output.println("componentLength=listLength;");			
		}
		else {
			throw new Exception("BERHelper.switchEncodeComponent: Code generation not supported for Type " + type.getName());
		}
	}
	
	
	private void switchEncodeListElement(Type elementType, String elementClassName, String componentName) throws Exception {
		String componentGetter = "instance.get" + Utils.uNormalize(componentName) + "()";

		if(elementType.isRestrictedCharacterStringType()) {
			output.println("componentLength+=writer.writeRestrictedCharacterString(" + componentGetter + ".get(i));");			
		}
		else if(elementType.isIntegerType()) {
			output.println("componentLength+=writer.writeInteger(" + componentGetter + ".get(i));");			
		}
		else if(elementType.isBooleanType()) {
			output.println("componentLength+=writer.writeBoolean(" + componentGetter + ".get(i));");			
		}
		else if(elementType.isBitStringType()) {
			output.println("componentLength+=writer.writeBitString(" + componentGetter + ".get(i));");			
		}
		else if(elementType.isOctetStringType()) {
			output.println("componentLength+=writer.writeOctetString(" + componentGetter + ".get(i));");			
		}
		else if(elementType.isObjectIdentifierType()) {
			output.println("componentLength+=writer.writeObjectIdentifier(" + componentGetter + ".get(i));");			
		}
		else if(elementType.isRelativeOIDType()) {
			output.println("componentLength+=writer.writeRelativeOID(" + componentGetter + ".get(i));");			
		}
		else if(elementType.isNullType()) {
			// do nothing
		}
		else if(elementType.isEnumeratedType()) {
			output.println("int intValue=-1;");
			output.println("switch(" + componentGetter + ".get(i)) {");
			EnumeratedType enumeratedType = (EnumeratedType)elementType;
			for(NamedNumber namedNumber : enumeratedType.getRootEnumeration()) {
				output.println("case " + Utils.normalizeConstant(namedNumber.getName()) + ":");
				output.println("intValue=" + namedNumber.getNumber() + ";");
				output.println("break;");
			}
			if(enumeratedType.getAdditionalEnumeration() != null) {
				for(NamedNumber namedNumber : enumeratedType.getAdditionalEnumeration()) {
					output.println("case " + Utils.normalizeConstant(namedNumber.getName()) + ":");
					output.println("intValue=" + namedNumber.getNumber() + ";");
					output.println("break;");
				}
			}
			output.println("}");
			output.println("componentLength+=writer.writeInteger(intValue);");			
		}
		else if(Utils.isConstructed(elementType)) {
			output.println("componentLength+=" + elementClassName + ".write(" + componentGetter + ".get(i),writer);");						
		}
		else {
			throw new Exception("BERHelper.processListOfTypeAssignment: Code generation not supported for Type " + elementType.getName());
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
			output.println(componentSetter + "reader.readRestrictedCharacterString(componentLength));");
		}
		else if(builtinType.isIntegerType()) {
			output.println(componentSetter + "reader.readInteger(componentLength));");
		}
		else if(builtinType.isBooleanType()) {
			output.println(componentSetter + "reader.readBoolean(componentLength));");
		}	
		else if(builtinType.isBitStringType()) {
			output.println(componentSetter + "reader.readBitString(componentLength));");
		}
		else if(builtinType.isOctetStringType()) {
			output.println(componentSetter + "reader.readOctetString(componentLength));");
		}
		else if(builtinType.isObjectIdentifierType()) {
			output.println(componentSetter + "reader.readObjectIdentifier(componentLength));");
		}
		else if(builtinType.isRelativeOIDType()) {
			output.println(componentSetter + "reader.readRelativeOID(componentLength));");
		}
		else if(builtinType.isNullType()) {
			output.println(componentSetter + "new Object());");
		}
		else if(builtinType.isEnumeratedType()) {
			EnumeratedType enumeratedType = (EnumeratedType)builtinType;
			String enumSuffix = "";
			if(type.isTypeReference() || componentName.equals("value")) {
				enumSuffix = ".Enum";
			}
			output.println("{");
			output.println("int intValue=reader.readInteger(componentLength);");
			for(NamedNumber namedNumber : enumeratedType.getRootEnumeration()) {
				output.println("if(intValue ==" + namedNumber.getNumber() + "){");
				output.println(componentSetter + componentClassName + enumSuffix + "." + Utils.normalizeConstant(namedNumber.getName()) + ");");
				output.println("}");
			}
			if(enumeratedType.getAdditionalEnumeration() == null) {
				output.println("if(" + componentGetter + "==null){");
				output.println("throw new Exception(\"Invalid enumeration value: \" + intValue);");
				output.println("}");
			}
			else {
				for(NamedNumber namedNumber : enumeratedType.getAdditionalEnumeration()) {
					output.println("if(intValue ==" + namedNumber.getNumber() + "){");
					output.println(componentSetter + componentClassName + enumSuffix + "." + Utils.normalizeConstant(namedNumber.getName()) + ");");
					output.println("}");
				}
				output.println("// Extensible: this.getValue() can return null if unknown enum value is decoded.");
			}
			output.println("}");
		}
		else if(type.isTypeReference()) {
			output.println(componentSetter + "new " + componentClassName + "());");		
			output.println(componentClassName + ".read(" + componentGetter + ",reader, componentLength);");		
		}
		else if(type.isTypeWithComponents()) {
			output.println(componentSetter + "new " + Utils.uNormalize(componentName) + "());");		
			output.println(Utils.uNormalize(componentName) + ".read(" + componentGetter + ",reader, componentLength);");		
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
			output.println(componentSetter + "new java.util.ArrayList<" + javaType + ">());");
			output.println("{");
			output.println("int listLength=componentLength;");
			output.println("int keepComponentLength=componentLength;");			
			output.println("while(listLength > 0 || listLength==-1) {");
			output.println("reader.readTag();");
			output.println("if(listLength!=-1) listLength-=reader.getTagLength();");
			writeElementTagsDecode(listOfType.getElement().getType());
			output.println("componentLength=reader.getLengthValue();");
			
			switchDecodeListElement(elementType, elementClassName, componentName, javaType);
			
			output.println("if(listLength!=-1) listLength-=componentLength;");
			output.println("}");
			output.println("componentLength=keepComponentLength;");			
			output.println("}");
		}
		else {
			throw new Exception("BERHelper.switchDecodeComponent: Code generation not supported for Type " + type.getName());
		}
	}
	
	
	private void switchDecodeListElement(Type elementType, String elementClassName, String componentName, String javaType) throws Exception {
		String componentGetter = "instance.get" + Utils.uNormalize(componentName) + "()";

		if(elementType.isRestrictedCharacterStringType()) {
			output.println(componentGetter + ".add(reader.readRestrictedCharacterString(componentLength));");	
		}
		else if(elementType.isIntegerType()) {
			output.println(componentGetter + ".add(reader.readInteger(componentLength));");	
		}
		else if(elementType.isBooleanType()) {
			output.println(componentGetter + ".add(reader.readBoolean(componentLength));");	
		}
		else if(elementType.isBitStringType()) {
			output.println(componentGetter + ".add(reader.readBitString(componentLength));");	
		}
		else if(elementType.isOctetStringType()) {
			output.println(componentGetter + ".add(reader.readOctetString(componentLength));");	
		}
		else if(elementType.isObjectIdentifierType()) {
			output.println(componentGetter + ".add(reader.readObjectIdentifier(componentLength));");	
		}
		else if(elementType.isRelativeOIDType()) {
			output.println(componentGetter + ".add(reader.readRelativeOID(componentLength));");	
		}
		else if(elementType.isNullType()) {
			output.println(componentGetter + ".add(new Object());");	
		}
		else if(elementType.isEnumeratedType()) {
			EnumeratedType enumeratedType = (EnumeratedType)elementType;
			output.println("int intValue=reader.readInteger(componentLength);");
			output.println(javaType + " item=null;");
			for(NamedNumber namedNumber : enumeratedType.getRootEnumeration()) {
				output.println("if(intValue ==" + namedNumber.getNumber() + "){");
				output.println("item=" + javaType + "." + Utils.normalizeConstant(namedNumber.getName()) + ";");
				output.println("}");
			}
			output.println("if(item!=null){");
			output.println(componentGetter + ".add(item);");
			output.println("}");
			if(enumeratedType.getAdditionalEnumeration() == null) {
				output.println("else {");
				output.println("throw new Exception(\"Invalid enumeration value: \" + intValue);");
				output.println("}");
			}
			else {
				for(NamedNumber namedNumber : enumeratedType.getAdditionalEnumeration()) {
					output.println("if(intValue ==" + namedNumber.getNumber() + "){");
					output.println("item=" + javaType + "." + Utils.normalizeConstant(namedNumber.getName()) + ";");
					output.println(componentGetter + ".add(item);");
					output.println("}");
				}
				output.println("// Extensible: instance.getValue() can return null if unknown enum value is decoded.");
			}
		}
		else if(Utils.isConstructed(elementType)) {
			if(elementType.isChoiceType()) {
				if(elementType.getTagList() != null && elementType.getTagList().size() != 0) {
					output.println("reader.readTag();");
					output.println("if (listLength != -1) listLength -= reader.getTagLength();");
					output.println("reader.readLength();");
					output.println("if (listLength != -1) listLength -= reader.getLengthLength();");
					output.println("componentLength=reader.getLengthValue();");
				}
				output.println("reader.setTagMatched(false);");
			}
			output.println(componentGetter + ".add(new " + javaType + "());");						
			output.println(javaType + ".read(" +  componentGetter + ".get(" + componentGetter + ".size()-1),reader, componentLength);");			
		}

	}

}
