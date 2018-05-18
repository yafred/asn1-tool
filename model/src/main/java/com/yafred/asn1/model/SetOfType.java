package com.yafred.asn1.model;


public class SetOfType extends ListOfType {
	
    public SetOfType(NamedType element) {
    	super(element);
    }

    @Override
	public boolean isSetOfType() {
        return true;
    }
    
    @Override
	public Tag getUniversalTag() {
        return new Tag(Integer.valueOf(17), TagClass.UNIVERSAL_TAG, null);
    }
    
	@Override
	public String getName() {
		return ("SET OF");
	}
}
