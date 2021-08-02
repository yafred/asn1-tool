package com.yafred.asn1.model.constraint;

import java.util.ArrayList;

public class AllExcept extends ConstraintElement {

	private ArrayList<ConstraintElement> exceptElements;
	
	public ArrayList<ConstraintElement> getExceptElements() {
		return exceptElements;
	}

	public void setExceptElements(ArrayList<ConstraintElement> exceptElements) {
		this.exceptElements = exceptElements;
	}

	@Override
	public boolean isAllExcept() {
		return true;
	}
}
