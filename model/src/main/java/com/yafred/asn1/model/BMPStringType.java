package com.yafred.asn1.model;

public class BMPStringType extends RestrictedCharacterStringType {

    @Override
	public boolean isBMPStringType() {
        return true;
    }

	@Override
	public Tag getUniversalTag() {
        return new Tag(Integer.valueOf(30), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("BMPString");
	}
}
