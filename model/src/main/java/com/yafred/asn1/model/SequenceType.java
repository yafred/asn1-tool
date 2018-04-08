package com.yafred.asn1.model;

import java.util.ArrayList;


public class SequenceType extends TypeWithComponents {

    public SequenceType(ArrayList<Component> rootComponentList,
    		ArrayList<Component> extensionComponentList,
    		ArrayList<Component> additionalComponentList) {
    	super(rootComponentList, extensionComponentList, additionalComponentList);
    }

    @Override
	public boolean isSequenceType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return new Tag(new Integer(16), TagClass.UNIVERSAL_TAG, null);
    }
     
    @Override
	public String getName() {
    	return "SEQUENCE";
    }
}
