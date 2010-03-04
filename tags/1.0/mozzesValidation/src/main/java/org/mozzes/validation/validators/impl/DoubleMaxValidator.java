/*
 * Copyright 2010 Mozzart
 *
 *
 * This file is part of mozzes.
 *
 * mozzes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mozzes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mozzes.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
