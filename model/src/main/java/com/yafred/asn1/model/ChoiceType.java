package com.yafred.asn1.model;

import java.util.ArrayList;


public class ChoiceType extends TypeWithComponents {
	private ArrayList<NamedType> alternativeListIncludingNested = null ; 

    public ChoiceType(ArrayList<Component> rootAlternativeList,
    		ArrayList<Component> additionalAlternativeList) {

        super(rootAlternativeList, additionalAlternativeList, null);
    }

    public ArrayList<Component> getRootAlternativeList() {
		return getRootComponentList();
	}
    
    public void setRootAlternativeList(ArrayList<Component> rootAlternativeList) {
    	setRootComponentList(rootAlternativeList);
    }

	public ArrayList<Component> getAdditionalAlternativeList() {
		return getExtensionComponentList();
	}
	
	public void setAdditionalAlternativeList(ArrayList<Component> additionalAlternativeList) {
		setExtensionComponentList(additionalAlternativeList);
	}

	public ArrayList<NamedType> getAlternativeListIncludingNested() {
		return alternativeListIncludingNested;
	}

	public void setAlternativeListIncludingNested(ArrayList<NamedType> alternativeListIncludingNested) {
		this.alternativeListIncludingNested = alternativeListIncludingNested;
	}

	@Override
	public boolean isChoiceType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return null;
    }
    
	@Override
	public String getName() {
		return ("CHOICE");
	}
}
