package com.yafred.asn1.model;


abstract public class ListOfType extends Type { 
	
	NamedType element;

	public ListOfType(NamedType namedType) {
		this.element = namedType;
	}
	
	public NamedType getElement() {
		return element;
	}


	public void setElement(NamedType element) {
		this.element = element;
	}


	@Override
	public boolean isListOfType() {
    	return true;
    }
}
