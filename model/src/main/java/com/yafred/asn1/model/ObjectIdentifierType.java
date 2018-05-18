package com.yafred.asn1.model;

public class ObjectIdentifierType extends Type {

    @Override
	public boolean isObjectIdentifierType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return new Tag(Integer.valueOf(6), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("OBJECT IDENTIFIER");
	}
}
