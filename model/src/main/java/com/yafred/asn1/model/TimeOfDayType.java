package com.yafred.asn1.model;

public class TimeOfDayType extends RestrictedCharacterStringType {

    public boolean isTimeOfDayType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return new Tag(Integer.valueOf(32), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("TIME-OF-DAY");
	}
}
