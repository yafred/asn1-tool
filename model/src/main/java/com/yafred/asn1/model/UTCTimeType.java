package com.yafred.asn1.model;

public class UTCTimeType extends Type {

    @Override
	public boolean isUTCTimeType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return new Tag(new Integer(23), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("UTCTime");
	}
}
