package com.yafred.asn1.model;


public class SequenceOfType extends ListOfType { 
	
    public SequenceOfType(NamedType element) {
    	super(element);
    }

    @Override
	public boolean isSequenceOfType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return new Tag(Integer.valueOf(16), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("SEQUENCE OF");
	}
}
