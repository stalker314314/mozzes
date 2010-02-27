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

import java.util.List;

import org.mozzes.invocation.common.interceptor.InvocationInterceptor;

import com.google.inject.Binder;

/**
 * The Class ApplicationModule is the base class that other custom modules can extend.<br>
 * Application modules can provide some,  services and interceptors and also do some binding.
 */
public abstract class ApplicationModule {

	/**
	 * @return the services that custom module provides.
	 */
	public List<ServiceConfiguration<?>> getServices() {
		return null;
	}

	/**
	 * @return the service interceptors that this module provides. Interceptors included here are
	 * invoked only for service call defined in this module (see {@link #getServices()}).
	 */
	public List<Class<? extends InvocationInterceptor>> getServiceInterceptors() {
		return null;
	}

	/**
	 * @return the server listeners that respond to the Mozzes server lifecycle events.
	 */
	public List<Class<? extends ServerLifecycleListener>> getServerListeners() {
		return null;
	}

	/**
	 * do some custom binding if is required for Guice to work properly
	 * 
	 * @param binder used in the custom binding
	 */
	public void doCustomBinding(Binder binder) {
		// default empty implementation
	}
	
	protected <E> void addService(List<ServiceConfiguration<?>> services, Class<E> spec, Class<? extends E> impl) {
		addService(services, spec, impl, false);
	}
	
	protected <E> void addService(List<ServiceConfiguration<?>> services, Class<E> spec, Class<? extends E> impl, boolean internal) {
		services.add(new ServiceConfiguration<E>(spec, impl, internal));
	}
}
