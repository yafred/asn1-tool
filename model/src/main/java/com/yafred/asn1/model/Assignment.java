package com.yafred.asn1.model;

public abstract class Assignment  {
    private String reference = null;
    private TokenLocation referenceTokenLocation = null;

    public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public TokenLocation getReferenceTokenLocation() {
		return referenceTokenLocation;
	}

	public void setReferenceTokenLocation(TokenLocation referenceTokenLocation) {
		this.referenceTokenLocation = referenceTokenLocation;
	}

	public boolean isTypeAssignment() {
        return false;
    }

    public boolean isValueAssignment() {
        return false;
    }
}
