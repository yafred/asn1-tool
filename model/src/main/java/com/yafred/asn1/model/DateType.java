package com.yafred.asn1.model;

public class DateType extends RestrictedCharacterStringType {

    @Override
	public boolean isDateType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return new Tag(new Integer(31), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("DATE");
	}
}
