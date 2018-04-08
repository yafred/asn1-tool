package com.yafred.asn1.model;

public class IntegerValue extends Value {
	int value = 0;
	
	public IntegerValue(int value) {
		this.value = value;
	}
	
    public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public boolean isIntegerValue() {
        return true;
    }

    @Override
	public String toString() {
    	return Integer.toString(value, 10);
    }
}
