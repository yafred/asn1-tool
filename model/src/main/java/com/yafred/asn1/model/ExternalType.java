package com.yafred.asn1.model;

public class ExternalType extends Type {

    @Override
	public boolean isExternalType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return new Tag(new Integer(8), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("EXTERNAL");
	}
}
