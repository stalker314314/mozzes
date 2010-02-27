package org.mozzes.rest.jersey.guice;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.sun.jersey.server.spi.component.ResourceComponentProviderFactoryClass;

@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ResourceComponentProviderFactoryClass(MozzesGuiceProviderFactory.class)
/**
 * @author stalker
 */
public @interface MozzesGuiceFactory {
}