package org.mozzes.swing.mgf.validation;

import javax.validation.Validation;
import javax.validation.Validator;

public class ValidationFactory {

	private ValidationFactory() {
	}

	public static Validator getValidator() {
		return Validation.buildDefaultValidatorFactory().getValidator();
	}
}
