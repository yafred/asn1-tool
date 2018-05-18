package com.yafred.asn1.model;

public class BooleanType extends Type {

    @Override
	public boolean isBooleanType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return new Tag(Integer.valueOf(1), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("BOOLEAN");
	}
}
