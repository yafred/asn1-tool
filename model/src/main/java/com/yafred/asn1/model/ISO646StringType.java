package com.yafred.asn1.model;

public class ISO646StringType extends VisibleStringType {
    
    @Override
	public boolean isISO646StringType() {
        return true;
    }

	@Override
	public String getName() {
		return ("ISO646String");
	}
}
