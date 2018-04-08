package com.yafred.asn1.model;

public class UTF8StringType extends RestrictedCharacterStringType {
 
    @Override
	public boolean isUTF8StringType() {
        return true;
    }

	@Override
	public Tag getUniversalTag() {
        return new Tag(new Integer(12), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("UTF8String");
	}
}
