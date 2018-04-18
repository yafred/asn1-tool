package com.yafred.asn1.model;

public class TimeType extends RestrictedCharacterStringType {

    public boolean isTimeType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return new Tag(new Integer(14), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("TIME");
	}
}
