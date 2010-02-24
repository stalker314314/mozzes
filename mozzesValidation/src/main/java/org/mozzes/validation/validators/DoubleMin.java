package org.mozzes.validation.validators;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.mozzes.validation.validators.impl.DoubleMinValidator;


@Target( { METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { DoubleMinValidator.class })
public @interface DoubleMin {
	String message() default "{javax.validation.constraints.DecimalMin.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * @return value the element must be higher or equal to
	 */
	double value();

	/**
	 * Defines several <code>@DoubleMin</code> annotations on the same element
	 * 
	 * @see DoubleMin
	 */
	@Target( { METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
	@Retention(RUNTIME)
	@Documented
	@interface List {
		DoubleMin[] value();
	}
}
