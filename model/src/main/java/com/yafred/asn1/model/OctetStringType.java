package com.yafred.asn1.model;

public class OctetStringType extends Type {

	@Override
	public boolean isOctetStringType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return new Tag(Integer.valueOf(4), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("OCTET STRING");
	}
}
