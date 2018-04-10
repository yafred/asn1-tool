package com.yafred.asn1.model;

public class SelectionType extends Type {
	private String selection = null;
	private Type type = null;
	private Type selectedType = null;
	private TokenLocation tokenLocation = null;
	
	public SelectionType(String selection, Type type) {
		this.selection = selection;
		this.type = type;
	}
	
	
    public String getSelection() {
		return selection;
	}


	public void setSelection(String selection) {
		this.selection = selection;
	}


	public Type getType() {
		return type;
	}


	public void setType(Type type) {
		this.type = type;
	}


	public Type getSelectedType() {
		return selectedType;
	}


	public void setSelectedType(Type selectedType) {
		this.selectedType = selectedType;
	}


	public TokenLocation getTokenLocation() {
		return tokenLocation;
	}

	public void setTokenLocation(TokenLocation tokenLocation) {
		this.tokenLocation = tokenLocation;
	}


	@Override
	public boolean isSelectionType() {
        return true;
    }

    @Override
	public Tag getUniversalTag() {
        return null;
    }
    
    @Override
	public String getName() {
    	return "";
    }
}
