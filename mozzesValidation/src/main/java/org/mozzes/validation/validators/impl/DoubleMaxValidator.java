package org.mozzes.validation.validators.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.mozzes.validation.validators.DoubleMax;


public class DoubleMaxValidator implements ConstraintValidator<DoubleMax, Double> {

	private double maxValue;

	public void initialize(DoubleMax maxValue) {
		this.maxValue = maxValue.value();
	}

	public boolean isValid(Double value, ConstraintValidatorContext constraintValidatorContext) {
		// null values are valid
		if (value == null) {
			return true;
		} else {
			double doubleValue = value.doubleValue();
			return doubleValue <= maxValue;
		}
	}

}
