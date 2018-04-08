package com.yafred.asn1.model;

abstract public class Component  {
	Token token = null;
	
	public Token getToken() {
		return token;
	}
	public void setToken(Token token) {
		this.token = token;
	}
	public boolean isNamedType() { return false; }
	public boolean isComponentsOf() { return false; }
}
