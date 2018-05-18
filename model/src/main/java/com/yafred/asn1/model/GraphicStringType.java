package com.yafred.asn1.model;

public class GraphicStringType extends RestrictedCharacterStringType {

    @Override
	public boolean isGraphicStringType() {
        return true;
    }
    
	@Override
	public Tag getUniversalTag() {
        return new Tag(Integer.valueOf(25), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("GraphicString");
	}
}
