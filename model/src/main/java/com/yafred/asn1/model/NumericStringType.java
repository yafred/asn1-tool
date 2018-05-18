package com.yafred.asn1.model;

public class NumericStringType extends RestrictedCharacterStringType {
 
    @Override
	public boolean isNumericStringType() {
        return true;
    }

	@Override
	public Tag getUniversalTag() {
        return new Tag(Integer.valueOf(18), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("NumericString");
	}
}
