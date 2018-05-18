package com.yafred.asn1.model;

import java.util.ArrayList;


public class BitStringType extends Type {

	private ArrayList<NamedNumber> namedBitList = null;
	
	public BitStringType() {
    }

	public BitStringType(ArrayList<NamedNumber> namedBitList) {
        this.namedBitList = namedBitList;
    }

    public void setNamedBitList(ArrayList<NamedNumber> namedBitList) {
        this.namedBitList = namedBitList;
    }

    public ArrayList<NamedNumber> getNamedBitList() {
		return namedBitList;
	}

	@Override
	public boolean isBitStringType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return new Tag(Integer.valueOf(3), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("BIT STRING");
	}
}
