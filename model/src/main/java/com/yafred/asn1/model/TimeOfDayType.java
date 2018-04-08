package com.yafred.asn1.model;

public class TimeOfDayType extends Type {

    public boolean isTimeOfDayType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return new Tag(new Integer(32), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("TIME-OF-DAY");
	}
}
