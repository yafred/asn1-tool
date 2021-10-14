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

import com.yafred.asn1.model.Tag;
import com.yafred.asn1.model.Type;

public class BERHelper {
	Generator generator;
	PrintWriter output;

	BERHelper(Generator generator) {
		this.generator = generator;
	}

	void processType(Type type, String className, boolean isInnerType) throws Exception {
		this.output = generator.output; // for now, write encoding/decoding methods in the POJO class

		output.println("// BER encoding methods for " + className);

		if (!isInnerType) {
	        // readPdu method
			output.println("func (value *" + className + ") ReadPdu(reader *ber.Reader) {");
			writePduTagsDecode(type);
			output.println("}");

			// writePdu method
			output.println("func (value *" + className + ") WritePdu(writer *ber.Writer) {");
			output.println("}");
		}
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
				byte[] tagBytes = tagHelper.getByteArray();
				
				String tagBytesAsString = "" + tagBytes[0];
				
				for(int i=1; i<tagBytes.length; i++) {
					tagBytesAsString += "," + tagBytes[i];
				}
				output.println("reader.ReadTag();");
				output.println(
						"// reader.mustMatchTag(new byte[] {" + tagBytesAsString + "}); /* " + tagHelper.toString() + " */");
				
				output.println("reader.ReadLength();");
			}
		}
	}	

}
