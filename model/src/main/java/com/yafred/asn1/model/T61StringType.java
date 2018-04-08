package com.yafred.asn1.model;

public class T61StringType extends TeletexStringType {
    
    @Override
	public boolean isT61StringType() {
        return true;
    }

	@Override
	public String getName() {
		return ("T61StringType");
	}
}
