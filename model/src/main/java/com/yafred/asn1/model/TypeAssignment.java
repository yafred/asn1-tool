package com.yafred.asn1.model;

public class TypeAssignment extends Assignment {
    private Type type = null;

    public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public boolean isTypeAssignment() {
        return true;
    }
}
