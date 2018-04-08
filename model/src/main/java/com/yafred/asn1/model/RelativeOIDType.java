package com.yafred.asn1.model;

public class RelativeOIDType extends Type {

    public boolean isRelativeOIDType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return new Tag(new Integer(13), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("RELATIVE-OID");
	}
}
