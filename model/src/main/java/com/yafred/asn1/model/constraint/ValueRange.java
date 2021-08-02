package com.yafred.asn1.model.constraint;

import com.yafred.asn1.model.Value;

public class ValueRange extends ConstraintElement {
	
	Value lowerEndValue;
	Value upperEndValue;
	
	@Override
	public boolean isValueRange() {
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
