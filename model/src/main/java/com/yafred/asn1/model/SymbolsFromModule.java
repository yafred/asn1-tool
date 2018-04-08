package com.yafred.asn1.model;

import java.util.ArrayList;


public class SymbolsFromModule { 

    /**
     * List of symbols (TextNode) imported from the module identified by moduleReference
     */
    private ArrayList<String> symbolList = null;

    /**
     * Global reference (name + objectIdentifier) of the module
     */
    private GlobalModuleReference moduleReference = null;

    
    
    public ArrayList<String> getSymbolList() {
		return symbolList;
	}

	public void setSymbolList(ArrayList<String> symbolList) {
		this.symbolList = symbolList;
	}

	public GlobalModuleReference getModuleReference() {
		return moduleReference;
	}

	public void setModuleReference(GlobalModuleReference moduleReference) {
		this.moduleReference = moduleReference;
	}

	public SymbolsFromModule(ArrayList<String> symbols,
        GlobalModuleReference moduleReference) {
        this.symbolList = symbols;
        this.moduleReference = moduleReference;
    }
}
