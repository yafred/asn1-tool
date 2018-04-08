package com.yafred.asn1.model;

public class SelectionType extends Type {
	private String selection = null;
	private Type type = null;
	
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
