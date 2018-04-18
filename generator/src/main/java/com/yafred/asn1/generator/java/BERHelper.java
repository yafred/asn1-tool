package com.yafred.asn1.generator.java;

import java.util.ArrayList;

import com.yafred.asn1.model.BooleanType;
import com.yafred.asn1.model.IntegerType;
import com.yafred.asn1.model.NullType;
import com.yafred.asn1.model.OctetStringType;
import com.yafred.asn1.model.RestrictedCharacterStringType;
import com.yafred.asn1.model.Tag;
import com.yafred.asn1.model.Type;
import com.yafred.asn1.model.TypeAssignment;

public class BERHelper {
	Generator generator;
	final static private String BER_READER = "com.yafred.asn1.runtime.BERReader";
	final static private String BER_WRITER = "com.yafred.asn1.runtime.BERWriter";

	public BERHelper(Generator generator) {
		this.generator = generator;
		
	}

	void processTypeAssignment(TypeAssignment typeAssignment, String className) throws Exception {
        ArrayList<Tag> tagList = Utils.getTagChain(typeAssignment.getType());
		
		// readPdu method
		generator.output.println("public static " + generator.packageName + "." + className + " readPdu(" + BER_READER
				+ " reader) throws Exception {");
		writeTagsDecode(typeAssignment.getType());
		String lengthText = "reader.getLengthValue()";

		if (tagList == null || tagList.size() == 0) { // it is an untagged CHOICE
			lengthText = "0";
		}

		generator.output.println(generator.packageName + "." + className + " ret = new " +
				generator.packageName + "." + className + "();");

		generator.output.println("ret.read(reader, " + lengthText + ");");
		generator.output.println("return ret;");
		generator.output.println("}");

		// writePdu method
		generator.output.println("public static void writePdu(" + generator.packageName + "." + className + " pdu, "
				+ BER_WRITER + " writer) throws Exception {");
        String lengthDeclaration = "";
        if (tagList != null && tagList.size() != 0) { // it is not an untagged CHOICE
            lengthDeclaration = "int length = ";
        }
        generator.output.println(lengthDeclaration + "pdu.write(writer);");
		writeTagsEncode(typeAssignment.getType());
		generator.output.println("writer.flush();");
		generator.output.println("}");
	}
	
	void processIntegerType(IntegerType integerType, String className) throws Exception {
	    // write encoding code
		generator.output.println("int write(" + BER_WRITER +
            " writer) throws Exception {");
		generator.output.println("return writer.writeInteger(this.getValue());");
		generator.output.println("}");

        // write decoding code
		generator.output.println("void read(" + BER_READER +
            " reader, int length) throws Exception {");
		generator.output.println("this.setValue(reader.readInteger(length));");
		generator.output.println("}");
	}
	
	void processBooleanType(BooleanType booleanType, String className) throws Exception {
	    // write encoding code
		generator.output.println("int write(" + BER_WRITER +
	            " writer) throws Exception {");
		generator.output.println("return writer.writeBoolean(this.getValue());");
		generator.output.println("}");

        // write decoding code
		generator.output.println("void read(" + BER_READER +
	            " reader, int length) throws Exception {");
		generator.output.println("this.setValue(reader.readBoolean(length));");
		generator.output.println("}");
	}
	
	void processOctetStringType(OctetStringType octetStringType, String className) throws Exception {
	    // write encoding code
		generator.output.println("int write(" + BER_WRITER +
	            " writer) throws Exception {");
		generator.output.println("return writer.writeOctetString(this.getValue());");
		generator.output.println("}");

        // write decoding code
		generator.output.println("void read(" + BER_READER +
	            " reader, int length) throws Exception {");
		generator.output.println("this.setValue(reader.readOctetString(length));");
		generator.output.println("}");
	}

	void processRestrictedCharacterStringType(RestrictedCharacterStringType restrictedCharacterStringType, String className) throws Exception {
	    // write encoding code
		generator.output.println("int write(" + BER_WRITER +
	            " writer) throws Exception {");
		generator.output.println("return writer.writeRestrictedCharacterString(this.getValue());");
		generator.output.println("}");

		// write decoding code
		generator.output.println("void read(" + BER_READER + " reader, int length) throws Exception {");
		generator.output.println("this.setValue(reader.readRestrictedCharacterString(length));");
		generator.output.println("}");
	}

	void processNullType(NullType nullType, String className) throws Exception {
	    // write encoding code
		generator.output.println("int write(" + BER_WRITER +
	            " writer) throws Exception {");
		generator.output.println("return 0;");
		generator.output.println("}");

        // write decoding code
		generator.output.println("void read(" + BER_READER + " reader, int length) throws Exception {");
		generator.output.println("this.setValue(new java.lang.Object());"); // dummy value
		generator.output.println("}");
	}
		
	private void writeTagsEncode(Type type) throws Exception {
		ArrayList<Tag> tagList = Utils.getTagChain(type);
		if (tagList != null && tagList.size() != 0) { // it is not an untagged CHOICE
			for (int iTag = tagList.size() - 1; iTag >= 0; iTag--) {
				boolean isConstructedForm = true;

				if ((iTag == (tagList.size() - 1)) && !Utils.isConstructed(type)) {
					isConstructedForm = false;
				}

				TagHelper tagHelper = new TagHelper(tagList.get(iTag), !isConstructedForm);
				generator.output.println("length += writer.writeLength(length);");

				byte[] tagBytes = tagHelper.getByteArray();
				
				for(int i=tagBytes.length-1; i>=0; i--) {
					generator.output.println(
							"length += writer.writeByte((byte)" + tagBytes[i] + ");");
				}
				generator.output.println("/* " + tagHelper.toString() + " */");
			}
		}
	}

	private void writeTagsDecode(Type type) throws Exception {
		ArrayList<Tag> tagList = Utils.getTagChain(type);
		if (tagList != null && tagList.size() != 0) { // it is not an untagged CHOICE
			for (int iTag = 0; iTag < tagList.size(); iTag++) {
				boolean isConstructedForm = true;

				if ((iTag == (tagList.size() - 1)) && !Utils.isConstructed(type)) {
					isConstructedForm = false;
				}

				TagHelper tagHelper = new TagHelper(tagList.get(iTag), !isConstructedForm);
				generator.output.println("/* " + tagHelper.toString() + " */");
				byte[] tagBytes = tagHelper.getByteArray();
				
				for(int i=0; i<tagBytes.length; i++) {
					generator.output.println(
							"reader.expectByte((byte)" + tagBytes[i] + ");");
				}
				
				generator.output.println("reader.readLength();");
			}
		}
	}
}
