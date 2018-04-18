package com.yafred.asn1.model;

public class GeneralizedTimeType extends RestrictedCharacterStringType {

    @Override
	public boolean isGeneralizedTimeType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return new Tag(new Integer(24), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("GeneralizedTime");
	}
}
