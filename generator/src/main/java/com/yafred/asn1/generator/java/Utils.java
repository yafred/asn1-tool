/*******************************************************************************
 * Copyright (C) 2018 Fred D7e (https://github.com/yafred)
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.yafred.asn1.model.Tag;
import com.yafred.asn1.model.TagMode;
import com.yafred.asn1.model.Type;
import com.yafred.asn1.model.TypeReference;

public class Utils {

	/**
	 * ASN.1 name to package name
	 */
	public static String normalizeConstant(String name) {
		return name.replace('-', '_').toUpperCase();
	}
	
	/**
	 * ASN.1 name to package name
	 */
	public static String normalizePackageName(String name) {
		return name.replace('-', '_');
	}
		
	/**
	 * ASN.1 name to Java name
	 */
	public static String normalize(String name) {
		StringBuffer buffer = new StringBuffer();
		boolean forceUppercase = false;
		for (int i = 0; i < name.length(); i++){
		    char c = name.charAt(i);        
		    if(c == '-') {
		    	forceUppercase = true;
		    }
		    else {
		    	if(forceUppercase) {
		    		c = Character.toUpperCase(c);
		    		forceUppercase = false;
		    	}
		    	buffer.append(c);
		    }
		}
		return buffer.toString();
	}

	/**
	 * ASN.1 name to Java name and forces 1rst letter to Uppercase
	 */
	public static String uNormalize(String name) {
		char[] temp = name.toCharArray();
		temp[0] = Character.toUpperCase(temp[0]);

		String retName = new String(temp);

		return normalize(retName);
	}
	
	public static String mapToJava(Type type) throws Exception {
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
		} else if (type.isObjectIdentifierType() || type.isRelativeOIDType()) {
			javaType = "long[]";
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

		if (type.isTypeWithComponents()|| type.isListOfType()) {
			ret = true;
		}

		return ret;
	}
		
	public static ArrayList<Tag> getTagChain(Type type) {
		ArrayList<Tag> tagList = new ArrayList<Tag>();
		
		if (type.isTypeReference()) {
			tagList.addAll(((TypeReference) type).getFullTagList());
			Type builtinType = ((TypeReference) type).getBuiltinType();
			if(!builtinType.isChoiceType()) {
				tagList.add(builtinType.getUniversalTag());
			}
			
		} else {
			if (type.getTagList() != null) {
				tagList.addAll(type.getTagList());
			}
			if (type.getUniversalTag() != null) {
				tagList.add(type.getUniversalTag());
			}
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
	
	public static <E> void addAllIfNotNull(List<E> list, Collection<? extends E> c) {
	    if (c != null) {
	        list.addAll(c);
	    }
	}
	

}
