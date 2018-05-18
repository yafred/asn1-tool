package com.yafred.asn1.model;

public class RelativeIRIType extends Type {

    public boolean isRelativeIRIType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return new Tag(Integer.valueOf(36), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("RELATIVE-OID-IRI");
	}
}
