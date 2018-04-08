
package com.yafred.asn1.model;

import java.util.ArrayList;

public class ModuleIdentifier {
	/**
	 * Name of the module.
	 */
	private String moduleReference;
	
	/**
	 * Location of the token in input stream
	 */
	private Token moduleReferenceToken;

	/**
	 * List of DefinitiveObjIdComponents
	 * A least 2 items (called 'arc') if not null.
	 */
	private ArrayList<DefinitiveObjectIdComponent> definitiveObjIdComponents;
	

	public String getModuleReference() {
		return moduleReference;
	}

	public void setModuleReference(String moduleReference) {
		this.moduleReference = moduleReference;
	}

	public Token getModuleReferenceToken() {
		return moduleReferenceToken;
	}

	public void setModuleReferenceToken(Token moduleReferenceToken) {
		this.moduleReferenceToken = moduleReferenceToken;
	}

	public ArrayList<DefinitiveObjectIdComponent> getDefinitiveObjIdComponents() {
		return definitiveObjIdComponents;
	}

	public void setDefinitiveObjIdComponents(ArrayList<DefinitiveObjectIdComponent> definitiveObjIdComponents) {
		this.definitiveObjIdComponents = definitiveObjIdComponents;
	}
}
