package com.yafred.asn1.model;

import java.util.ArrayList;

public class TypeReference extends Type {
    /**
     * Name of the module where the assignment should exist.
     * Provided by specification.
     * Optional.
     */
    private String referencedModuleName = null;

	/**
	 * Location of the token in input stream
	 */
	private TokenLocation tokenLocation;

    /**
     * Name of the assignment which defines this type.
     * Provided by specification.
     * Mandatory.
     */
    private String referencedTypeName = null;
    
	private Type referencedType = null;

    public String getReferencedModuleName() {
		return referencedModuleName;
	}

	public void setReferencedModuleName(String referencedModuleName) {
		this.referencedModuleName = referencedModuleName;
	}

	public String getReferencedTypeName() {
		return referencedTypeName;
	}

	public void setReferencedTypeName(String referencedTypeName) {
		this.referencedTypeName = referencedTypeName;
	}	
	
	public Type getReferencedType() {
		return referencedType;
	}

	public void setReferencedType(Type referencedType) {
		this.referencedType = referencedType;
	}
	
	public TokenLocation getTokenLocation() {
		return tokenLocation;
	}

	public void setTokenLocation(TokenLocation tokenLocation) {
		this.tokenLocation = tokenLocation;
	}

	/**
     * Get the builtin type that eventually defines this type
     */
	public Type getBuiltinType() {
		Type ret = null;
		if(referencedType != null) {
			if(referencedType.isTypeReference()) {
				ret = ((TypeReference)referencedType).getBuiltinType();
			}
			else {
				ret = referencedType;
			}
		}
		return ret;
	}
	
	/**
	 * Possibly recursive, grouping all the tagLists
	 */
    public ArrayList<Tag> getFullTagList() {
    	ArrayList<Tag> fullTagList = new ArrayList<Tag>();
    	if(tagList != null) {
    		fullTagList.addAll(tagList);
    	}
		if(referencedType != null) {
			if(referencedType.isTypeReference()) {
				fullTagList.addAll(((TypeReference)referencedType).getFullTagList());
			}
			else {
				if(referencedType.getTagList() != null) {
					fullTagList.addAll(referencedType.getTagList());
				}
			}
		}		
		return fullTagList;
	}

	@Override
	public boolean isTypeReference() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return null;
    }
    
	@Override
	public String getName() {
		String name = "";
		if(referencedModuleName != null) {
			name = referencedModuleName + ".";
		}
		return name + referencedTypeName;
	}
}
