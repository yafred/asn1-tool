package com.yafred.asn1.model;

public class ValueRangeConstraint extends ConstraintSpec {
	
	Value lowerEndValue;
	Value upperEndValue;
	
	@Override
	public boolean isValueRangeConstraint() {
		return true;
	}

	public Value getLowerEndValue() {
		return lowerEndValue;
	}

	public void setLowerEndValue(Value lowerEndValue) {
		this.lowerEndValue = lowerEndValue;
	}

	public Value getUpperEndValue() {
		return upperEndValue;
	}

	public void setUpperEndValue(Value upperEndValue) {
		this.upperEndValue = upperEndValue;
	}
}
