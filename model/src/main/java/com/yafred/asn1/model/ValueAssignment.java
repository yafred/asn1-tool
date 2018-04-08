package com.yafred.asn1.model;

public class ValueAssignment extends Assignment {
    private Value value = null;

    public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}

	@Override
	public boolean isValueAssignment() {
        return true;
    }
}
