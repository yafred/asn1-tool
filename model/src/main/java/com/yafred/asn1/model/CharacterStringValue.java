package com.yafred.asn1.model;

public class CharacterStringValue extends Value {
	String value = ""; // value is quoted "This is a text"
	
	public CharacterStringValue(String value) {
		this.value = value;
	}
	
    @Override
	public boolean isCharacterStringValue() {
        return true;
    }

    @Override
	public String toString() {
    	return value;
    }
}
