package com.yafred.asn1.model;

import java.util.ArrayList;

public class IntegerType extends Type {
	
	ArrayList<NamedNumber> namedNumberList;
	
	public IntegerType() {
		
	}
	
	public void setNamedNumberList(ArrayList<NamedNumber> namedNumberList) {
		this.namedNumberList = namedNumberList;
	}

	public ArrayList<NamedNumber> getNamedNumberList() {
		return namedNumberList;
	}

	@Override
	public boolean isIntegerType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return new Tag(Integer.valueOf(2), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("INTEGER");
	}
}
