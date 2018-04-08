package com.yafred.asn1.model;

public class EmptyListValue extends Value {
	

    public boolean isEmptyListValue() {
        return true;
    }
    
    @Override
	public String toString() {
    	return "{ }";
    }   
}
