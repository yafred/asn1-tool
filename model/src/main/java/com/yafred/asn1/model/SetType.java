package com.yafred.asn1.model;

import java.util.ArrayList;


public class SetType extends TypeWithComponents {

    public SetType(ArrayList<Component> rootComponentList,
    		ArrayList<Component> extensionComponentList,
    		ArrayList<Component> additionalComponentList) {
    	super(rootComponentList, extensionComponentList, additionalComponentList);
    }

    @Override
	public boolean isSetType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return new Tag(Integer.valueOf(17), TagClass.UNIVERSAL_TAG, null);
    }

     @Override
	public String getName() {
     	return "SET";
     }
}


