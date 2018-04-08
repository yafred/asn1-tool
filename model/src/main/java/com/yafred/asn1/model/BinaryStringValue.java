package com.yafred.asn1.model;

public class BinaryStringValue extends Value {
	String value = ""; // value is quoted '01010'B
	
	public BinaryStringValue(String value) {
		this.value = value;
	}
	
    public boolean isBitStringValue() {
        return true;
    }

    @Override
	public String toString() {
    	return value;
    }
}
