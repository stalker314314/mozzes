package org.mozzes.validation.validators.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.mozzes.validation.validators.DoubleMin;


public class DoubleMinValidator implements ConstraintValidator<DoubleMin, Double> {
	private double minValue;

	public void initialize(DoubleMin minValue) {
		this.minValue = minValue.value();
	}

	public boolean isValid(Double value, ConstraintValidatorContext constraintValidatorContext) {
		// null values are valid
		if (value == null) {
			return true;
		} else {
			return value >= minValue;
		}

	}
}
