package org.mozzes.application.module.scope;

import com.google.inject.ScopeAnnotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Annotation TransactionScoped is used for annotating classes that should have single instance in the transaction
 * and different instances in the different transactions.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ScopeAnnotation
public @interface TransactionScoped {
	// scope annotation
}
