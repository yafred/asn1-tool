package com.yafred.asn1.model;


public class SequenceOfType extends ListOfType { 
	
    public SequenceOfType(String elementName, Type type) {
    	super(elementName, type);
    }

    @Override
	public boolean isSequenceOfType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return new Tag(new Integer(16), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("SEQUENCE OF");
	}
}
