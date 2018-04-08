package com.yafred.asn1.model;

public abstract class Assignment  {
    private String reference = null;
    private Token referenceToken = null;

    public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Token getReferenceToken() {
		return referenceToken;
	}

	public void setReferenceToken(Token referenceToken) {
		this.referenceToken = referenceToken;
	}

	public boolean isTypeAssignment() {
        return false;
    }

    public boolean isValueAssignment() {
        return false;
    }
}
