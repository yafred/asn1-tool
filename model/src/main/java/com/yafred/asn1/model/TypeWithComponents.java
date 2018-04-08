package com.yafred.asn1.model;

import java.util.ArrayList;


abstract public class TypeWithComponents extends Type {
    private ArrayList<Component> rootComponentList = null;
    private ArrayList<Component> extensionComponentList = null;
    private ArrayList<Component> additionalComponentList = null;
    
    private boolean automaticTaggingSelected = false;

    public TypeWithComponents(ArrayList<Component> rootComponentList,
    		ArrayList<Component> extensionComponentList,
    		ArrayList<Component> additionalComponentList) {
    	this.rootComponentList = rootComponentList;
    	this.extensionComponentList = extensionComponentList;
    	this.additionalComponentList = additionalComponentList;
    }

    public ArrayList<Component> getRootComponentList() {
		return rootComponentList;
	}

	public void setRootComponentList(ArrayList<Component> rootComponentList) {
		this.rootComponentList = rootComponentList;
	}

	public ArrayList<Component> getExtensionComponentList() {
		return extensionComponentList;
	}

	public void setExtensionComponentList(ArrayList<Component> extensionComponentList) {
		this.extensionComponentList = extensionComponentList;
	}

	public ArrayList<Component> getAdditionalComponentList() {
		return additionalComponentList;
	}

	public void setAdditionalComponentList(ArrayList<Component> additionalComponentList) {
		this.additionalComponentList = additionalComponentList;
	}

	public boolean isAutomaticTaggingSelected() {
		return automaticTaggingSelected;
	}

	public void setAutomaticTaggingSelected(boolean automaticTaggingSelected) {
		this.automaticTaggingSelected = automaticTaggingSelected;
	}
   
    @Override
	public boolean isTypeWithComponents() {
    	return true;
    }
}
