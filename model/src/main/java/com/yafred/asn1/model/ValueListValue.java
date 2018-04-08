package com.yafred.asn1.model;

import java.util.ArrayList;

public class ValueListValue extends Value {
	ArrayList<Value> valueList; 
	
	public ValueListValue(ArrayList<Value> valueList) {
		this.valueList = valueList;
	}
	
    public ArrayList<Value> getValueList() {
		return valueList;
	}

	public void setValueList(ArrayList<Value> valueList) {
		this.valueList = valueList;
	}

	@Override
	public boolean isValueList() {
        return true;
    }
}
