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
	private TokenLocation tokenLocation;

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
	
	public TokenLocation getTokenLocation() {
		return tokenLocation;
	}

	public void setTokenLocation(TokenLocation tokenLocation) {
		this.tokenLocation = tokenLocation;
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
