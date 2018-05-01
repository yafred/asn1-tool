package com.yafred.asn1.model;


abstract public class ListOfType extends Type { 
	String elementName;
	Type elementType;
	
    public ListOfType(String elementName, Type elementType) {
    	this.elementName = elementName;
    	this.elementType = elementType;
    }

    public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	public Type getElementType() {
		return elementType;
	}

	public void setElementType(Type elementType) {
		this.elementType = elementType;
	}

	@Override
	public boolean isListOfType() {
    	return true;
    }
}
