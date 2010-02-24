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
