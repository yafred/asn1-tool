package com.yafred.asn1.model.constraint;

public abstract class ConstraintElement {

	public boolean isUnions() {
		return false;
	}
	
	public boolean isAllExcept() {
		return false;
	}
	
	public boolean isIntersection() {
		return false;
	}
	
	public boolean isIntersectionElements() {
		return false;
	}
	
	public boolean isValueRange() {
		return false;
	}

	public boolean isSize() {
		return false;
	}
}
