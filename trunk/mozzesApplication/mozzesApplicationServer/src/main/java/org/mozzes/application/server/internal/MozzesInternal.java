/*
 * Copyright 2010 Mozzart
 *
 *
 * This file is part of mozzes.
 *
 * mozzes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mozzes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mozzes.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.mozzes.application.server.internal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.mozzes.application.server.session.impl.SessionContext;

import com.google.inject.BindingAnnotation;

/**
 * The Interface MozzesInternal is used in guice configuration for specifying that some internal framework classes(for
 * example {@link SessionContext}) can be injected only if this annotation is present. <br>
 * 
 * With this mechanism we provided some level of security that users at least can't inject some internal classes easily.
 * 
 * @see <a
 *      href="http://google-guice.googlecode.com/svn/trunk/javadoc/index.html?com/google/inject/BindingAnnotation.html">BindingAnnotationAPI</a>
 */
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@BindingAnnotation
public @interface MozzesInternal {
  // binding annotation
}
