package com.yafred.asn1.model;

public class IRIType extends Type {

    @Override
	public boolean isIRIType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return new Tag(Integer.valueOf(35), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("OID-IRI");
	}
}
