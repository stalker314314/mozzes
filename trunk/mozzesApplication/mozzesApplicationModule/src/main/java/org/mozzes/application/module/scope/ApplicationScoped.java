package org.mozzes.application.module.scope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.inject.*;

/**
 * The Annotation ApplicationScoped is used for annotating classes that should have single instance in the application
 * regardless of the users,session,requests etc.<br>
 * 
 * (this is the annotation for {@link Scopes}.SINGLETON scope
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ScopeAnnotation
public @interface ApplicationScoped {
	// scope annotation
}
