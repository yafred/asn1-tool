package com.yafred.asn1.model;


abstract public class ListOfType extends Type { 
	String elementName;
	Type type;
	
    public ListOfType(String elementName, Type type) {
    	this.elementName = elementName;
    	this.type = type;
    }

    public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public boolean isListOfType() {
    	return true;
    }
}
