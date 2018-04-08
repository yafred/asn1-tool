package com.yafred.asn1.model;

public class SelectionType extends Type {
	private String selection = null;
	private Type type = null;
	private Type selectedType = null;
	private Token token = null;
	
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


	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
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
