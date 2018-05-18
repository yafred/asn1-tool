package com.yafred.asn1.model;

public class PrintableStringType extends RestrictedCharacterStringType {
 
    @Override
	public boolean isPrintableStringType() {
        return true;
    }

	@Override
	public Tag getUniversalTag() {
        return new Tag(Integer.valueOf(19), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("PrintableString");
	}
}
