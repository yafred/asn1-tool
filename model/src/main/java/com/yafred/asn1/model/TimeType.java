package com.yafred.asn1.model;

public class TimeType extends RestrictedCharacterStringType {

    public boolean isTimeType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return new Tag(Integer.valueOf(14), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("TIME");
	}
}
