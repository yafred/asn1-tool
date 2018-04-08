package com.yafred.asn1.model;

public class ValueReference extends Value {
    /**
     * Name of the module where the assignment should exist.
     * Provided by specification.
     * Optional.
     */
    private String referencedModuleName = null;

    /**
     * Name of the assignment which defines this value.
     * Provided by specification.
     * Mandatory.
     */
    private String referencedValueName = null;

	/**
	 * Location of the token in input stream
	 */
	private Token token;

	private Value referencedValue = null;

    public String getReferencedModuleName() {
		return referencedModuleName;
	}

	public void setReferencedModuleName(String referencedModuleName) {
		this.referencedModuleName = referencedModuleName;
	}

	public String getReferencedValueName() {
		return referencedValueName;
	}

	public void setReferencedValueName(String referencedValueName) {
		this.referencedValueName = referencedValueName;
	}
		
	public Value getReferencedValue() {
		return referencedValue;
	}

	public void setReferencedValue(Value referencedValue) {
		this.referencedValue = referencedValue;
	}
	
	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	@Override
	public boolean isValueReference() {
        return true;
    }
   
    @Override
	public String toString() {
 		String name = "";
		if(referencedModuleName != null) {
			name = referencedModuleName + ".";
		}
		return name + referencedValueName;
	}
}
