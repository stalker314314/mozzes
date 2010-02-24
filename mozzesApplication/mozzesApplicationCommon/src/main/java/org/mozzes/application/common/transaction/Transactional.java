package org.mozzes.application.common.transaction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that annotated service method will start new transaction. <br>
 * The Transactional annotation is used when user wants to execute something in the new nested transaction regardless of
 * the overall transaction that's already associated with the request.
 * 
 * Note: This annotation should be applied to the service interface specification not in the implementation class
 * 
 * @author Perica Milosevic
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Transactional {
	// marker annotation
}
