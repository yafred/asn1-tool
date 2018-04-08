package com.yafred.asn1.model;

public class IA5StringType extends RestrictedCharacterStringType {

    @Override
	public boolean isIA5StringType() {
        return true;
    }

 	@Override
	public Tag getUniversalTag() {
        return new Tag(new Integer(22), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("IA5String");
	}
}
