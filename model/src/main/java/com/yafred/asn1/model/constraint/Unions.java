package com.yafred.asn1.model.constraint;

import java.util.ArrayList;

public class Unions extends ConstraintElement {
	
	private ArrayList<Intersection> intersections;

	public ArrayList<Intersection> getIntersections() {
		return intersections;
	}

	public void setIntersections(ArrayList<Intersection> intersections) {
		this.intersections = intersections;
	}

	@Override
	public boolean isUnions() {
		return true;
	}
}
