package com.yafred.asn1.model.constraint;

public class Size extends ConstraintElement {
	private ConstraintElement constraintElement; 

	
	public ConstraintElement getConstraintElement() {
		return constraintElement;
	}

	public void setConstraintElement(ConstraintElement constraintElement) {
		this.constraintElement = constraintElement;
	}
	
	public boolean isSize() {
		return true;
	}
}
