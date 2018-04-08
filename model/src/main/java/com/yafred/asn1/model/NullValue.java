package com.yafred.asn1.model;

public class NullValue extends Value {
	

    @Override
	public boolean isNullValue() {
        return true;
    }

    @Override
	public String toString() {
    	return "NULL";
    }
 }
