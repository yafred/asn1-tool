package com.yafred.asn1.model;

public class GlobalModuleReference {
	
	private String name = null;
	private ObjectIdentifierValue objectIdentifierValue = null; 	
	private String definedValue = null;
	
	public GlobalModuleReference(String name) {
		this.name = name;
	}
	
	public GlobalModuleReference(String name, String definedValue) {
		this.name = name;
		this.definedValue = definedValue;
	}

	public GlobalModuleReference(String name, ObjectIdentifierValue objectIdentifierValue) {
		this.name = name;
		this.objectIdentifierValue = objectIdentifierValue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ObjectIdentifierValue getObjectIdentifierValue() {
		return objectIdentifierValue;
	}

	public void setObjectIdentifierValue(ObjectIdentifierValue objectIdentifierValue) {
		this.objectIdentifierValue = objectIdentifierValue;
	}

	public String getDefinedValue() {
		return definedValue;
	}

	public void setDefinedValue(String definedValue) {
		this.definedValue = definedValue;
	}



}
