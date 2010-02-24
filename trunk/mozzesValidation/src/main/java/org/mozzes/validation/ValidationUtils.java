package org.mozzes.validation;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

public class ValidationUtils {

	public static Validator getValidator() {
		return ValidationFactory.getValidator();
	}

	public static boolean isValid(Object object, Class<?>... groups) {
		return getValidator().validate(object, groups).isEmpty();
	}

	public static String[] getViolationMessages(Object object, Class<?>... groups) {
		Set<ConstraintViolation<Object>> violations = getValidator().validate(object, groups);
		String[] messages = new String[violations.size()];
		int i = 0;
		for (ConstraintViolation<Object> violation : violations) {
			messages[i++] = violation.getMessage();
		}
		return messages;
	}
}
