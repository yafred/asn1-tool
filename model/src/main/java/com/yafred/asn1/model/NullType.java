package com.yafred.asn1.model;

public class NullType extends Type {

    @Override
	public boolean isNullType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return new Tag(Integer.valueOf(5), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("NULL");
	}
}
