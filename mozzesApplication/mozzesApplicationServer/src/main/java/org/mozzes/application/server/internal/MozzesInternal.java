package org.mozzes.application.server.internal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;

/**
 * The Interface MozzesInternal is used in guice configuration for specifying that some internal framework classes(for
 * example {@linkSessionContext}) can be injected only if this annotation is present. <br>
 * 
 * With this mechanism we provided some level of security that users at least can't inject some internal classes easily.
 * 
 * @see <a
 *      href="http://google-guice.googlecode.com/svn/trunk/javadoc/index.html?com/google/inject/BindingAnnotation.html">BindingAnnotationAPI</a>
 */
@Target( { ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@BindingAnnotation
public @interface MozzesInternal {
	// binding annotation
}
