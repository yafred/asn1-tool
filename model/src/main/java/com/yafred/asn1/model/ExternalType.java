package com.yafred.asn1.model;

public class ExternalType extends Type {

    @Override
	public boolean isExternalType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return new Tag(Integer.valueOf(8), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("EXTERNAL");
	}
}
