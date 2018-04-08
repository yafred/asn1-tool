package com.yafred.asn1.model;

public class DurationType extends Type {

    @Override
	public boolean isDurationType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return new Tag(new Integer(34), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("DURATION");
	}
}
