package com.yafred.asn1.model;

public class HexaStringValue extends Value {
	String value = ""; // value is quoted 'A0B1F'H
	
	public HexaStringValue(String value) {
		this.value = value;
	}
	
    public boolean isOctetStringValue() {
        return true;
    }

    @Override
	public String toString() {
    	return value;
    }
}
