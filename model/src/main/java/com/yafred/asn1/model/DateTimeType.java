package com.yafred.asn1.model;

public class DateTimeType extends RestrictedCharacterStringType {

    @Override
	public boolean isDateTimeType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return new Tag(new Integer(33), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("DATE-TIME");
	}
}
