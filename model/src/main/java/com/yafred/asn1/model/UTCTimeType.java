package com.yafred.asn1.model;

public class UTCTimeType extends RestrictedCharacterStringType {

    @Override
	public boolean isUTCTimeType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return new Tag(Integer.valueOf(23), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("UTCTime");
	}
}
