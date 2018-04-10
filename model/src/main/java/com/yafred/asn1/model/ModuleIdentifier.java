
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
	private TokenLocation moduleReferenceTokenLocation;

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

	public TokenLocation getModuleReferenceTokenLocation() {
		return moduleReferenceTokenLocation;
	}

	public void setModuleReferenceTokenLocation(TokenLocation moduleReferenceTokenLocation) {
		this.moduleReferenceTokenLocation = moduleReferenceTokenLocation;
	}

	public ArrayList<DefinitiveObjectIdComponent> getDefinitiveObjIdComponents() {
		return definitiveObjIdComponents;
	}

	public void setDefinitiveObjIdComponents(ArrayList<DefinitiveObjectIdComponent> definitiveObjIdComponents) {
		this.definitiveObjIdComponents = definitiveObjIdComponents;
	}
}
