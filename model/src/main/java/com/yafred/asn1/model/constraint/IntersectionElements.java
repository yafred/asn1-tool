package com.yafred.asn1.model.constraint;

import java.util.ArrayList;

public class IntersectionElements extends ConstraintElement {
	
	private ArrayList<ConstraintElement> elements;
	private ArrayList<ConstraintElement> exceptElements;
	
	public ArrayList<ConstraintElement> getElements() {
		return elements;
	}

	public void setElements(ArrayList<ConstraintElement> elements) {
		this.elements = elements;
	}

	public ArrayList<ConstraintElement> getExceptElements() {
		return exceptElements;
	}

	public void setExceptElements(ArrayList<ConstraintElement> exceptElements) {
		this.exceptElements = exceptElements;
	}

	@Override
	public boolean isIntersectionElements() {
		return true;
	}
}
