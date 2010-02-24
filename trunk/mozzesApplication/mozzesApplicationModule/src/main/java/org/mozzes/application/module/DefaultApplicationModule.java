package org.mozzes.application.module;

import java.util.ArrayList;
import java.util.List;

import org.mozzes.invocation.common.interceptor.InvocationInterceptor;


/**
 * Default ApplicationModule implementation that doesn't require class extension and provides methods to add services,
 * interceptors, listeners and custom binding tasks.
 */
public class DefaultApplicationModule extends ApplicationModule {

	private final List<ServiceConfiguration<?>> services = new ArrayList<ServiceConfiguration<?>>();

	private final List<Class<? extends InvocationInterceptor>> serviceInterceptors = new ArrayList<Class<? extends InvocationInterceptor>>();

	private final List<Class<? extends ServerLifecycleListener>> serverListeners = new ArrayList<Class<? extends ServerLifecycleListener>>();

	public DefaultApplicationModule addService(ServiceConfiguration<?> serviceConfiguration) {
		services.add(serviceConfiguration);
		return this;
	}

	public DefaultApplicationModule addServiceInterceptor(Class<? extends InvocationInterceptor> serviceInterceptor) {
		serviceInterceptors.add(serviceInterceptor);
		return this;
	}

	public DefaultApplicationModule addServerListener(Class<? extends ServerLifecycleListener> serverListener) {
		serverListeners.add(serverListener);
		return this;
	}

	@Override
	public final List<ServiceConfiguration<?>> getServices() {
		return services;
	}

	@Override
	public final List<Class<? extends InvocationInterceptor>> getServiceInterceptors() {
		return serviceInterceptors;
	}

	@Override
	public final List<Class<? extends ServerLifecycleListener>> getServerListeners() {
		return serverListeners;
	}

}
