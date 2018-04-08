package com.yafred.asn1.model;

public class BooleanType extends Type {

    @Override
	public boolean isBooleanType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return new Tag(new Integer(1), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("BOOLEAN");
	}
}
