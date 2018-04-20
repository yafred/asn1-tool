package com.yafred.asn1.generator.java;

import java.util.ArrayList;

import com.yafred.asn1.model.Tag;
import com.yafred.asn1.model.TagMode;
import com.yafred.asn1.model.Type;
import com.yafred.asn1.model.TypeReference;

public class Utils {

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
		} else if (type.isBitStringType()) {
			javaType = "java.util.BitSet";
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
