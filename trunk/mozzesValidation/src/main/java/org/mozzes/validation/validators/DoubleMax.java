package org.mozzes.validation.validators;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.mozzes.validation.validators.impl.DoubleMaxValidator;


@Target( { METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { DoubleMaxValidator.class })
public @interface DoubleMax {
	String message() default "{javax.validation.constraints.DoubleMax.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * @return value the element must be lower or equal to
	 */
	double value();

	/**
	 * Defines several <code>@DoubleMax</code> annotations on the same element
	 * 
	 * @see DoubleMax
	 */
	@Target( { METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
	@Retention(RUNTIME)
	@Documented
	@interface List {
		DoubleMax[] value();
	}
}
