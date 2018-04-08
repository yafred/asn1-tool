package com.yafred.asn1.model;

public class ObjectDescriptorType extends Type {

    @Override
	public boolean isObjectDescriptorType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return new Tag(new Integer(7), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("ObjectDescriptor");
	}
}
