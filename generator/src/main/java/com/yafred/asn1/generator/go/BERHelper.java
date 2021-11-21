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

import java.io.PrintWriter;
import java.util.ArrayList;

import com.yafred.asn1.generator.common.TagHelper;
import com.yafred.asn1.generator.common.Utils;
import com.yafred.asn1.model.Tag;
import com.yafred.asn1.model.Type;

public class BERHelper {
	Generator generator;
	PrintWriter output;

	BERHelper(Generator generator) {
		this.generator = generator;
	}

	void processType(Type type, String className, boolean isComponent) throws Exception {
		this.output = generator.output; // for now, write encoding/decoding methods in the POJO class

		ArrayList<Tag> tagList = Utils.getTagChain(type);

		output.println("// BER encoding methods for " + className);

		if (!isComponent) {
	        // readPdu method
			output.println("func (value *" + className + ") ReadPdu(reader *ber.Reader) error {");
			writePduTagsDecode(type);
			output.println("return value.Read(reader, reader.GetLengthValue())");
			output.println("}");

			// writePdu method
			output.println("func (value *" + className + ") WritePdu(writer *ber.Writer) error {");
	        String lengthDeclaration = "";
	        if (tagList != null && tagList.size() != 0) { // it is not an untagged CHOICE
	            lengthDeclaration = "componentLength, error := ";
	        }
			else {
	            lengthDeclaration = "error := ";
			}
	        output.println(lengthDeclaration + "value.Write(writer);");
			writeTagsEncode(type);
			output.println("return error");
			output.println("}");
		}

		if (!type.isTypeWithComponents() && !type.isListOfType()) {
			// read method
			output.println("func (value *" + className + ") Read(reader *ber.Reader, componentLength int) error {");
			output.println("var error error = nil");
			switchDecodeComponent(type, "value", className);
			output.println("return error");
			output.println("}");

			// write method
			output.println("func (value *" + className + ") Write(writer *ber.Writer) (int, error) {");
			output.println("var componentLength int = 0");
			output.println("var error error = nil");
			switchEncodeComponent(type, "value", className);
			output.println("return componentLength, error");			
			output.println("}");
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

			output.println("componentLength += int(writer.WriteLength(uint32(componentLength)))"); // NEED sorting

			TagHelper tagHelper = new TagHelper(tagList.get(iTag), !isConstructedForm);	
			output.println(
						"componentLength += writer.WriteOctetString([]byte {" + tagHelper.tagBytesAsGoString() + "}); /* " + tagHelper.toString() + " */");
		}
	}


	private void writePduTagsDecode(Type type) throws Exception {
		ArrayList<Tag> tagList = Utils.getTagChain(type);
		if (tagList != null && tagList.size() != 0) { // it is not an untagged CHOICE
			output.println("var error error");
			for (int iTag = 0; iTag < tagList.size(); iTag++) {
				boolean isConstructedForm = true;

				if ((iTag == (tagList.size() - 1)) && !Utils.isConstructed(type)) {
					isConstructedForm = false;
				}

				TagHelper tagHelper = new TagHelper(tagList.get(iTag), !isConstructedForm);
				output.println("error = reader.ReadTag();");
				output.println("if error != nil {");
				output.println("return error");
				output.println("}");				
				output.println(
						"if !reader.MatchTag([]byte {" + tagHelper.tagBytesAsGoString() + "}) { /* " + tagHelper.toString() + " */");
				output.println("return errors.New(\"Expected tag: " + tagHelper.toString() + "\")");
				output.println("}");
				output.println("error = reader.ReadLength();");
				output.println("if error != nil {");
				output.println("return error");
				output.println("}");
			}
		}
	}	


	private void switchEncodeComponent(Type type, String componentName, String componentClassName) throws Exception {
		Type builtinType = type;
		if(builtinType.isIntegerType() || builtinType.isEnumeratedType()) {
			output.println("componentLength=writer.WriteInteger(int(*value))");			
		}
		if(builtinType.isBooleanType()) {
			output.println("componentLength=writer.WriteBoolean(bool(*value))");			
		}
		if(builtinType.isOctetStringType()) {
			output.println("componentLength=writer.WriteOctetString(*value)");			
		}
		if(builtinType.isRestrictedCharacterStringType()) {
			output.println("componentLength=writer.WriteRestrictedCharacterString(string(*value))");			
		}
		if(builtinType.isBitStringType()) {
			output.println("componentLength=writer.WriteBitString(value.bitString)");			
		}
	}


	private void switchDecodeComponent(Type type, String componentName, String componentClassName) throws Exception {
		Type builtinType = type;
		if(builtinType.isIntegerType() || builtinType.isEnumeratedType()) {
			output.println("intValue, error := reader.ReadInteger(componentLength)");
			output.println("if error == nil {");
			output.println("*value = "+componentClassName+"(intValue)");
			output.println("}");
		}
		if(builtinType.isBooleanType()) {
			output.println("boolValue, error := reader.ReadBoolean()");
			output.println("if error == nil {");
			output.println("*value = "+componentClassName+"(boolValue)");
			output.println("}");
		}
		if(builtinType.isOctetStringType()) {
			output.println("bytesValue, error := reader.ReadOctetString(componentLength)");
			output.println("if error == nil {");
			output.println("*value = "+componentClassName+"(bytesValue)");
			output.println("}");
		}
		if(builtinType.isRestrictedCharacterStringType()) {
			output.println("stringValue, error := reader.ReadRestrictedCharacterString(componentLength)");
			output.println("if error == nil {");
			output.println("*value = "+componentClassName+"(stringValue)");
			output.println("}");
		}
		if(builtinType.isBitStringType()) {
			output.println("bitStringValue, error := reader.ReadBitString(componentLength)"); // Needs coding
			output.println("if error == nil {");
			output.println("value.bitString = bitStringValue");
			output.println("}");
		}
	}
}
