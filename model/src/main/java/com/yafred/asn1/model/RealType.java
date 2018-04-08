package com.yafred.asn1.model;

public class RealType extends Type {

	@Override
	public boolean isRealType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return new Tag(new Integer(9), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("REAL");
	}
}
