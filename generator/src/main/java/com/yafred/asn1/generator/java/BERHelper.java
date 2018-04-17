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

		generator.output.println("return readValue(reader, " + lengthText + ");");
		generator.output.println("}");

		// writePdu method
		generator.output.println("public static void writePdu(" + generator.packageName + "." + className + " pdu, "
				+ BER_WRITER + " writer) throws Exception {");
        String lengthDeclaration = "";
        if (tagList != null && tagList.size() != 0) { // it is not an untagged CHOICE
            lengthDeclaration = "int length = ";
        }
        generator.output.println(lengthDeclaration + "writeValue(pdu, writer);");
		writeTagsEncode(typeAssignment.getType());
		generator.output.println("writer.flush();");
		generator.output.println("}");
	}
	
	void processIntegerType(IntegerType integerType, String className) throws Exception {
	    // write encoding code
		generator.output.println("public static int writeValue(" + generator.packageName + "." +
            className + " value, " + BER_WRITER +
            " writer) throws Exception {");
		generator.output.println("return writer.writeInteger(value.getValue());");
		generator.output.println("}");

        // write decoding code
		generator.output.println("public static " + generator.packageName + "." + className +
            " readValue(" + BER_READER +
            " reader, int length) throws Exception {");
		generator.output.println(generator.packageName + "." + className + " value = new " +
				generator.packageName + "." + className + "();");
		generator.output.println("value.setValue(reader.readInteger(length));");
		generator.output.println("return value;");
		generator.output.println("}");
	}
	
	void processBooleanType(BooleanType booleanType, String className) throws Exception {
	    // write encoding code
		generator.output.println("public static int writeValue(" + generator.packageName + "." +
            className + " value, " + BER_WRITER +
            " writer) throws Exception {");
		generator.output.println("return writer.writeBoolean(value.getValue());");
		generator.output.println("}");

        // write decoding code
		generator.output.println("public static " + generator.packageName + "." + className +
            " readValue(" + BER_READER +
            " reader, int length) throws Exception {");
		generator.output.println(generator.packageName + "." + className + " value = new " +
				generator.packageName + "." + className + "();");
		generator.output.println("value.setValue(reader.readBoolean(length));");
		generator.output.println("return value;");
		generator.output.println("}");
	}
	
	void processOctetStringType(OctetStringType octetStringType, String className) throws Exception {
	    // write encoding code
		generator.output.println("public static int writeValue(" + generator.packageName + "." +
            className + " value, " + BER_WRITER +
            " writer) throws Exception {");
		generator.output.println("return writer.writeOctetString(value.getValue());");
		generator.output.println("}");

        // write decoding code
		generator.output.println("public static " + generator.packageName + "." + className +
            " readValue(" + BER_READER +
            " reader, int length) throws Exception {");
		generator.output.println(generator.packageName + "." + className + " value = new " +
				generator.packageName + "." + className + "();");
		generator.output.println("value.setValue(reader.readOctetString(length));");
		generator.output.println("return value;");
		generator.output.println("}");
	}

	void processRestrictedCharacterStringType(RestrictedCharacterStringType restrictedCharacterStringType, String className) throws Exception {
	    // write encoding code
		generator.output.println("public static int writeValue(" + generator.packageName + "." +
            className + " value, " + BER_WRITER +
            " writer) throws Exception {");
		generator.output.println("return writer.writeRestrictedCharacterString(value.getValue());");
		generator.output.println("}");

        // write decoding code
		generator.output.println("public static " + generator.packageName + "." + className +
            " readValue(" + BER_READER +
            " reader, int length) throws Exception {");
		generator.output.println(generator.packageName + "." + className + " value = new " +
				generator.packageName + "." + className + "();");
		generator.output.println("value.setValue(reader.readRestrictedCharacterString(length));");
		generator.output.println("return value;");
		generator.output.println("}");
	}

	void processNullType(NullType nullType, String className) throws Exception {
	    // write encoding code
		generator.output.println("public static int writeValue(" + generator.packageName + "." +
            className + " value, " + BER_WRITER +
            " writer) throws Exception {");
		generator.output.println("return 0;");
		generator.output.println("}");

        // write decoding code
		generator.output.println("public static " + generator.packageName + "." + className +
            " readValue(" + BER_READER +
            " reader, int length) throws Exception {");
		generator.output.println(generator.packageName + "." + className + " value = new " +
				generator.packageName + "." + className + "();");
		generator.output.println("value.setValue(new java.lang.Object());");
		generator.output.println("return value;");
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

				generator.output.println("length += writer.writeLength(length);");

				if (tagList.get(iTag).getNumber().intValue() <= 30) {
					generator.output.println(
							"length += writer.writeByte(" + Utils.oneByteTagAsString(tagList.get(iTag), isConstructedForm)
									+ "); " + Utils.getCommentForTag(tagList.get(iTag), isConstructedForm));
				} else {
					throw new Exception("Not implemented (Tag > 30)");
				}
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

				if (tagList.get(iTag).getNumber().intValue() <= 30) {
					generator.output.println(
							"reader.expectByte(" + Utils.oneByteTagAsString(tagList.get(iTag), isConstructedForm)
									+ "); " + Utils.getCommentForTag(tagList.get(iTag), isConstructedForm));
				} else {
					throw new Exception("Not implemented (Tag > 30)");
				}

				generator.output.println("reader.readLength();");
			}
		}
	}
}
