package com.yafred.asn1.model;


public class SetOfType extends ListOfType {
	
    public SetOfType(String elementName, Type type) {
    	super(elementName, type);
    }

    @Override
	public boolean isSetOfType() {
        return true;
    }
    
    @Override
	public Tag getUniversalTag() {
        return new Tag(new Integer(17), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("SET OF");
	}
}
