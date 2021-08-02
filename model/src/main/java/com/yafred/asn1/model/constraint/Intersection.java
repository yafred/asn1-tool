package com.yafred.asn1.model.constraint;

import java.util.ArrayList;

public class Intersection extends ConstraintElement {
	
	private ArrayList<IntersectionElements> intersectionElements;
	
	public ArrayList<IntersectionElements> getIntersectionElements() {
		return intersectionElements;
	}

	public void setIntersectionElements(ArrayList<IntersectionElements> intersectionElements) {
		this.intersectionElements = intersectionElements;
	}

	@Override
	public boolean isIntersection() {
		return true;
	}
}
