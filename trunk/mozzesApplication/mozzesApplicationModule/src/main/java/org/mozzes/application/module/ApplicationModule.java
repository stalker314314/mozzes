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
