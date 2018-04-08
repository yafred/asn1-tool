package com.yafred.asn1.model;

import java.util.ArrayList;


/**
 * Contains all the modules involved in a specification.
 * Not actually an ASN.1 production.
 */
public class Specification {
	private ArrayList<ModuleDefinition> moduleDefinitionList = null;

    public ArrayList<ModuleDefinition> getModuleDefinitionList() {
		return moduleDefinitionList;
	}

	public void setModuleDefinitionList(ArrayList<ModuleDefinition> moduleDefinitionList) {
		this.moduleDefinitionList = moduleDefinitionList;
	}

	public Specification(ArrayList<ModuleDefinition> moduleDefinitionList) {
    	this.moduleDefinitionList = moduleDefinitionList;
    }
}
