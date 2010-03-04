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
package org.mozzes.validation;

import java.util.Locale;

import javax.validation.Configuration;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class ValidationFactory {
	private static ValidationFactory instance;

	private final Locale locale;
	private final Validator cachedValidator;

	private ValidationFactory(Locale locale) {
		this.locale = locale;
		cachedValidator = getValidatorFactory().getValidator();
	}

	public static void initialize() {
		instance = new ValidationFactory(Locale.getDefault());
	}

	public static void initialize(Locale locale) {
		instance = new ValidationFactory(locale);
	}

	private static ValidationFactory getInstance() {
		if (instance == null)
			initialize();
		return instance;
	}

	public static Validator getValidator() {
		return getInstance().cachedValidator;
	}

	private ValidatorFactory getValidatorFactory() {
		Configuration<?> config = Validation.byDefaultProvider().configure();
		config.messageInterpolator(new SlightlyModifiedDefaultMessageInterpolator(locale));
		ValidatorFactory factory = config.buildValidatorFactory();
		return factory;
	}
}
