package com.yafred.asn1.generator.java;

import java.util.ArrayList;

import com.yafred.asn1.model.Tag;
import com.yafred.asn1.model.TagClass;
import com.yafred.asn1.model.TagMode;
import com.yafred.asn1.model.Type;
import com.yafred.asn1.model.TypeReference;

class Utils {
	private static String[] byteTable = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c",
			"d", "e", "f" };

	/**
	 * ASN.1 name to Java name
	 */
	public static String normalize(String name) {
		return name.replace('-', '_');
	}

	/**
	 * ASN.1 name to Java name and forces 1rst letter to Uppercase
	 */
	public static String uNormalize(String name) {
		char[] temp = name.toCharArray();
		temp[0] = Character.toUpperCase(temp[0]);

		String retName = new String(temp);

		return retName.replace('-', '_');
	}
	
	public static String mapToJava(Type type, boolean isFullNameRequired) throws Exception {
		String javaType = "";

		if (type.isRestrictedCharacterStringType()) {
            javaType = "java.lang.String";
		} else if (type.isIntegerType()) {
			javaType = "java.lang.Integer";
		} else if (type.isBooleanType()) {
			javaType = "java.lang.Boolean";
		} else if (type.isNullType()) {
			javaType = "java.lang.Object";
		} else if (type.isOctetStringType()) {
			javaType = "byte[]";
		} else {
			throw new Exception("Type not mapped: " + type.getName());
		}

		return javaType;
	}
	
	public static boolean isConstructed(Type type) {
		boolean ret = false;

		while (type.isTypeReference()) {
			type = ((TypeReference) type).getReferencedType();
		}

		if (type.isChoiceType() || type.isSequenceType() || type.isSetType() || type.isSequenceOfType()
				|| type.isSetOfType()) {
			ret = true;
		}

		return ret;
	}
	
	public static String oneByteTagAsString(Tag tag, boolean isConstructedForm) throws Exception {
		int tagNumber = tag.getNumber().intValue();

		if (tagNumber > 30) {
			throw new Exception("Tag is not a one byte tag");
		}

		if(tag.getTagClass() == TagClass.UNIVERSAL_TAG) {
			tagNumber |= 0x00;
		}
		else if(tag.getTagClass() == TagClass.APPLICATION_TAG) {
			tagNumber |= 0x40;
		} else if(tag.getTagClass() == TagClass.PRIVATE_TAG) {
			tagNumber |= 0xc0;
		} else { // CONTEXT
			tagNumber |= 0x80;
		}

		if (isConstructedForm) {
			tagNumber |= 0x20;
		}

		return "(byte)0x" + byteTable[tagNumber / 16] + byteTable[tagNumber % 16];
	}

	/**
	 *
	 * @param tag
	 * @param isConstructedForm
	 * @return
	 */
	public static String getCommentForTag(Tag tag, boolean isConstructedForm) {
		return "/* " + (isConstructedForm ? "CONSTRUCTED_" : "PRIMITIVE_")
				+ (tag.getTagClass().toString().equals("") ? "CONTEXT" : tag.getTagClass().toString()) + "_"
				+ tag.getNumber() + " */";
	}
	
	public static ArrayList<Tag> getTagChain(Type type) {
		ArrayList<Tag> tagList = new ArrayList<Tag>();
		
		if(type.getUniversalTag() != null) {
			if(type.getTagList() != null) {
				tagList.addAll(type.getTagList());
			}
			tagList.add(type.getUniversalTag());
		}
		else if(type.isTypeReference()) {
			tagList.addAll(((TypeReference)type).getFullTagList());
		}
		
		ArrayList<Tag> ret = new ArrayList<Tag>();

		boolean isImplicit = false;
		for(Tag tag : tagList) {
			if(!isImplicit) {
				ret.add(tag);
			}
			else {
				isImplicit = false;
			}
			if(tag.getTagMode() == TagMode.IMPLICIT_TAG) {
				isImplicit = true;
			}
		}

		return ret;
	}
}
