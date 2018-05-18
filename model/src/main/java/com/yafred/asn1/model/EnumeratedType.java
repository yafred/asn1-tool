package com.yafred.asn1.model;

import java.util.ArrayList;


public class EnumeratedType extends Type {


    private ArrayList<NamedNumber> rootEnumeration = null;
    /*
     * If additionalEnumeration is non null, then type is extensible
     */
    private ArrayList<NamedNumber> additionalEnumeration = null;

    public EnumeratedType(ArrayList<NamedNumber> rootEnumeration, ArrayList<NamedNumber> additionalEnumeration) {
		this.rootEnumeration = rootEnumeration;
		this.additionalEnumeration = additionalEnumeration;
    }
    
    public ArrayList<NamedNumber> getRootEnumeration() {
		return rootEnumeration;
	}

	public void setRootEnumeration(ArrayList<NamedNumber> rootEnumeration) {
		this.rootEnumeration = rootEnumeration;
	}

	public ArrayList<NamedNumber> getAdditionalEnumeration() {
		return additionalEnumeration;
	}

	public void setAdditionalEnumeration(ArrayList<NamedNumber> additionalEnumeration) {
		this.additionalEnumeration = additionalEnumeration;
	}

	@Override
	public boolean isEnumeratedType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return new Tag(Integer.valueOf(10), TagClass.UNIVERSAL_TAG, null);
    }

	@Override
	public String getName() {
		return ("ENUMERATED");
	}
}
