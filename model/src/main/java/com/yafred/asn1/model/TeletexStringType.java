package com.yafred.asn1.model;

public class TeletexStringType extends RestrictedCharacterStringType {

    @Override
	public boolean isTeletexStringType() {
        return true;
    }

	@Override
	public Tag getUniversalTag() {
        return new Tag(Integer.valueOf(20), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("TeletexString");
	}
}
