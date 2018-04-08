package com.yafred.asn1.model;

public class VisibleStringType extends RestrictedCharacterStringType {

    @Override
	public boolean isVisibleStringType() {
        return true;
    }

	@Override
	public Tag getUniversalTag() {
        return new Tag(new Integer(26), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("VisibleString");
	}
}
