package com.yafred.asn1.model;

import java.util.ArrayList;

public class NamedValueListValue extends Value {
	ArrayList<NamedValue> valueList; 
	
	public NamedValueListValue(ArrayList<NamedValue> valueList) {
		this.valueList = valueList;
	}
	
    public ArrayList<NamedValue> getValueList() {
		return valueList;
	}

	public void setValueList(ArrayList<NamedValue> valueList) {
		this.valueList = valueList;
	}

	@Override
	public boolean isNamedValueList() {
        return true;
    }
}
