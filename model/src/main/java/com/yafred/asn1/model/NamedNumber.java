package com.yafred.asn1.model;

public class NamedNumber {
    private String name = null;
    private Integer number = null;
    private String reference = null; // reference to an integer value
    
    private Token nameToken;
    private Token numberOrReferenceToken;

    public NamedNumber(String name) { // for enumeratedType only
        this.name = name;
        this.number = null;
    }
    public NamedNumber(String name, Integer number) {
        this.name = name;
        this.number = number;
    }

    public NamedNumber(String name, String reference) {
        this.name = name;
        this.reference = reference;
    }

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public Token getNameToken() {
		return nameToken;
	}
	public void setNameToken(Token nameToken) {
		this.nameToken = nameToken;
	}
	public Token getNumberOrReferenceToken() {
		return numberOrReferenceToken;
	}
	public void setNumberOrReferenceToken(Token numberOrReferenceToken) {
		this.numberOrReferenceToken = numberOrReferenceToken;
	}
}
