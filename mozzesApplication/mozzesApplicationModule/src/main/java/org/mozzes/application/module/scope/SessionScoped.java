package org.mozzes.application.module.scope;

import com.google.inject.ScopeAnnotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Annotation SessionScoped is used for annotating classes that should have single instance in the same session and
 * different instances in the different sessions.<br>
 * The SessionScoped object will preserve the state of the client in between requests in the same session.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ScopeAnnotation
public @interface SessionScoped {
	// scope annotation
}
