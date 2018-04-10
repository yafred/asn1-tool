package com.yafred.asn1.model;

abstract public class Component  {
	TokenLocation tokenLocation = null;
	
	public TokenLocation getTokenLocation() {
		return tokenLocation;
	}
	public void setTokenLocation(TokenLocation token) {
		this.tokenLocation = token;
	}
	public boolean isNamedType() { return false; }
	public boolean isComponentsOf() { return false; }
}
