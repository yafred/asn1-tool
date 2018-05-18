package com.yafred.asn1.model;

public class CharacterStringType extends Type {

	@Override
	public boolean isCharacterStringType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return new Tag(Integer.valueOf(29), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("CHARACTER STRING");
	}
}
