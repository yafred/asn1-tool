package com.yafred.asn1.model;

public class ChoiceValue extends Value {
	String identifier = ""; 
	Value value;
	
	public ChoiceValue(String identifier, Value value) {
		this.identifier = identifier;
		this.value = value;
	}
	
    public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}

	@Override
	public boolean isChoiceValue() {
        return true;
    }
}
