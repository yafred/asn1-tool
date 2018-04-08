package com.yafred.asn1.model;

public class GraphicStringType extends RestrictedCharacterStringType {

    @Override
	public boolean isGraphicStringType() {
        return true;
    }
    
	@Override
	public Tag getUniversalTag() {
        return new Tag(new Integer(25), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("GraphicString");
	}
}
