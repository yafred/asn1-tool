package com.yafred.asn1.model;

public class EmbeddedPDVType extends Type {

    @Override
	public boolean isEmbeddedPDVType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return new Tag(new Integer(11), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("EMBEDDED PDV");
	}
}
