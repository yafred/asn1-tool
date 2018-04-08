package com.yafred.asn1.model;

public class GeneralStringType extends RestrictedCharacterStringType {
	
	@Override
	public boolean isGeneralStringType() {
		return true;
	}

	@Override
	public Tag getUniversalTag() {
        return new Tag(new Integer(27), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("GeneralString");
	}
}
