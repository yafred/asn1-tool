package com.yafred.asn1.model;

public class BooleanValue extends Value {
	boolean value = true;
	
	public BooleanValue(boolean value) {
		this.value = value;
	}
	
    @Override
	public boolean isBooleanValue() {
        return true;
    }

    @Override
	public String toString() {
		if(value == true) {
			return "TRUE";
		}
		else {
			return "FALSE";			
		}
    }
}
