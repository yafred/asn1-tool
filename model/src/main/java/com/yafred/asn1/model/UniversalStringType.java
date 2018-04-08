package com.yafred.asn1.model;

public class UniversalStringType extends RestrictedCharacterStringType {

    @Override
	public boolean isUniversalStringType() {
        return true;
    }

	@Override
	public Tag getUniversalTag() {
        return new Tag(new Integer(28), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("UniversalString");
	}
}
