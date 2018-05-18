package com.yafred.asn1.model;

public class RelativeOIDType extends Type {

    public boolean isRelativeOIDType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return new Tag(Integer.valueOf(13), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("RELATIVE-OID");
	}
}
