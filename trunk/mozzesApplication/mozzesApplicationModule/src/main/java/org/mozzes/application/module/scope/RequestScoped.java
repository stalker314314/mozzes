package org.mozzes.application.module.scope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.inject.ScopeAnnotation;

/**
 * The Annotation RequestScoped is used for annotating classes that should have single instance in the same request and
 * different instances in the different requests.<br>
 * The RequestScoped object will have the same state in the all transactions that are started in the same request.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ScopeAnnotation
public @interface RequestScoped {
	// scope annotation
}
