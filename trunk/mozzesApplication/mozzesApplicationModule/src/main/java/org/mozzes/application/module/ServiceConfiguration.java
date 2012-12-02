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
package org.mozzes.application.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.inject.Binder;
import com.google.inject.BindingAnnotation;
import com.google.inject.Key;

/**
 * The Class ServiceConfiguration contains information about server's service.
 * 
 * Service is something that exists on the server and can be called either by the client or by the other service and
 * thus provide some functionality. Service can also be "internal" which means that clients can't call the service and
 * only the other services can execute the service.
 */
public class ServiceConfiguration<I> {

  /** The interface that specifies the service behavior. */
  private final Class<I> serviceInterface;

  /** Class that implements I interface */
  private final Class<? extends I> serviceImplementation;

  /** Is the service internal */
  private final boolean internal;

  public ServiceConfiguration(Class<I> serviceInterface, Class<? extends I> serviceImplementation) {
    this(serviceInterface, serviceImplementation, false);
  }

  public ServiceConfiguration(Class<I> serviceInterface, Class<? extends I> serviceImplementation, boolean internal) {
    this.serviceInterface = serviceInterface;
    this.serviceImplementation = serviceImplementation;
    this.internal = internal;
  }

  public Key<? extends I> bind(Binder binder) {
    return bindImplementation(binder, serviceImplementation);
  }

  public Class<I> getServiceInterface() {
    return serviceInterface;
  }

  public Class<? extends I> getServiceImplementation() {
    return serviceImplementation;
  }

  public boolean isInternal() {
    return internal;
  }

  private <IMPL> Key<IMPL> bindImplementation(Binder binder, Class<IMPL> implementationClass) {
    Key<IMPL> serviceImplementationKey = Key.get(implementationClass, ServiceImplementation.class);
    binder.bind(serviceImplementationKey).to(implementationClass);
    return serviceImplementationKey;
  }

  /**
   * The Interface ServiceImplementation is annotation that serves as a guard for service implementation. If someone
   * wants to inject the serviceImplementation class rather than serviceInterface he would need this annotation but this
   * annotation is private so it's impossible to directly inject implementation surpassing interface.
   */
  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  @BindingAnnotation
  private @interface ServiceImplementation {
    // binding annotation
  }
}
