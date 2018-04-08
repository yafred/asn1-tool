package com.yafred.asn1.model;

public class VideotexStringType extends RestrictedCharacterStringType {

    @Override
	public boolean isVideotexStringType() {
        return true;
    }

 	@Override
	public Tag getUniversalTag() {
        return new Tag(new Integer(21), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("VideotexString");
	}
}
