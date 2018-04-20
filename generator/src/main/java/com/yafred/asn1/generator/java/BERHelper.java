package com.yafred.asn1.generator.java;

import java.util.ArrayList;

import com.yafred.asn1.model.BitStringType;
import com.yafred.asn1.model.BooleanType;
import com.yafred.asn1.model.EnumeratedType;
import com.yafred.asn1.model.IntegerType;
import com.yafred.asn1.model.NamedNumber;
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

	void processTypeAssignment(Type type, String className) throws Exception {
        ArrayList<Tag> tagList = Utils.getTagChain(type);
		
		// readPdu method
		generator.output.println("public static " + generator.packageName + "." + className + " readPdu(" + BER_READER
				+ " reader) throws Exception {");
		writeTagsDecode(type);
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
		writeTagsEncode(type);
		generator.output.println("writer.flush();");
		generator.output.println("}");
	}
	
	void processEnumeratedTypeAssignment(EnumeratedType enumeratedType, String className) throws Exception {
	    // write encoding code
		generator.output.println("int write(" + BER_WRITER +
            " writer) throws Exception {");
		generator.output.println("int intValue=-1;");
		generator.output.println("switch(getValue()) {");
		for(NamedNumber namedNumber : enumeratedType.getRootEnumeration()) {
			generator.output.println("case " + Utils.normalize(namedNumber.getName()) + ":");
			generator.output.println("intValue=" + namedNumber.getNumber() + ";");
			generator.output.println("break;");
		}
		if(enumeratedType.getAdditionalEnumeration() != null) {
			for(NamedNumber namedNumber : enumeratedType.getAdditionalEnumeration()) {
				generator.output.println("case " + Utils.normalize(namedNumber.getName()) + ":");
				generator.output.println("intValue=" + namedNumber.getNumber() + ";");
				generator.output.println("break;");
			}
		}
		generator.output.println("}");
		generator.output.println("return writer.writeInteger(intValue);");
		generator.output.println("}");

        // write decoding code
		generator.output.println("void read(" + BER_READER +
            " reader, int length) throws Exception {");
		generator.output.println("int intValue=reader.readInteger(length);");
		for(NamedNumber namedNumber : enumeratedType.getRootEnumeration()) {
			generator.output.println("if(intValue ==" + namedNumber.getNumber() + "){");
			generator.output.println("setValue(" + className + "Enum." + Utils.normalize(namedNumber.getName()) + ");");
			generator.output.println("}");
		}
		if(enumeratedType.getAdditionalEnumeration() == null) {
			generator.output.println("if(null == getValue()){");
			generator.output.println("throw new Exception(\"Invalid enumeration value: \" + intValue);");
			generator.output.println("}");
		}
		else {
			for(NamedNumber namedNumber : enumeratedType.getAdditionalEnumeration()) {
				generator.output.println("if(intValue ==" + namedNumber.getNumber() + "){");
				generator.output.println("setValue(" + className + "Enum." + Utils.normalize(namedNumber.getName()) + ");");
				generator.output.println("}");
			}
			generator.output.println("// Extensible: this.getValue() can return null if unknown enum value is decoded.");
		}
		generator.output.println("}");
		
		// pdu methods
		processTypeAssignment(enumeratedType, className);
	}
	
	void processIntegerTypeAssignment(IntegerType integerType, String className) throws Exception {
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
		
		// pdu methods
		processTypeAssignment(integerType, className);
	}

	void processBitStringTypeAssignment(BitStringType bitStringType, String className) throws Exception {
	    // write encoding code
		generator.output.println("int write(" + BER_WRITER +
            " writer) throws Exception {");
		generator.output.println("return writer.writeBitString(this.getValue());");
		generator.output.println("}");

        // write decoding code
		generator.output.println("void read(" + BER_READER +
            " reader, int length) throws Exception {");
		generator.output.println("this.setValue(reader.readBitString(length));");
		generator.output.println("}");
		
		// pdu methods
		processTypeAssignment(bitStringType, className);
	}

	void processBooleanTypeAssignment(BooleanType booleanType, String className) throws Exception {
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
		
		// pdu methods
		processTypeAssignment(booleanType, className);
	}
	
	void processOctetStringTypeAssignment(OctetStringType octetStringType, String className) throws Exception {
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
		
		// pdu methods
		processTypeAssignment(octetStringType, className);
	}

	void processRestrictedCharacterStringTypeAssignment(RestrictedCharacterStringType restrictedCharacterStringType, String className) throws Exception {
	    // write encoding code
		generator.output.println("int write(" + BER_WRITER +
	            " writer) throws Exception {");
		generator.output.println("return writer.writeRestrictedCharacterString(this.getValue());");
		generator.output.println("}");

		// write decoding code
		generator.output.println("void read(" + BER_READER + " reader, int length) throws Exception {");
		generator.output.println("this.setValue(reader.readRestrictedCharacterString(length));");
		generator.output.println("}");
		
		// pdu methods
		processTypeAssignment(restrictedCharacterStringType, className);
	}

	void processNullTypeAssignment(NullType nullType, String className) throws Exception {
	    // write encoding code
		generator.output.println("int write(" + BER_WRITER +
	            " writer) throws Exception {");
		generator.output.println("return 0;");
		generator.output.println("}");

        // write decoding code
		generator.output.println("void read(" + BER_READER + " reader, int length) throws Exception {");
		generator.output.println("this.setValue(new java.lang.Object());"); // dummy value
		generator.output.println("}");
		
		// pdu methods
		processTypeAssignment(nullType, className);
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
