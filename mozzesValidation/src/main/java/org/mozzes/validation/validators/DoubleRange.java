package org.mozzes.validation.validators;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

@Documented
@Constraint(validatedBy = { })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@DoubleMin(0)
@DoubleMax(Double.MAX_VALUE)
@ReportAsSingleViolation
public @interface DoubleRange {
	@OverridesAttribute(constraint = DoubleMin.class, name = "value")
	double min() default 0;

	@OverridesAttribute(constraint = DoubleMax.class, name = "value")
	double max() default Double.MAX_VALUE;

	String message() default "{org.hibernate.validator.constraints.Range.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
