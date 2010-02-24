package org.mozzes.application.server.service.impl;

import org.mozzes.application.module.ServerInitializationException;
import org.mozzes.application.module.ServerLifecycleListener;
import org.mozzes.application.server.service.ServerLifeCycleService;

import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * This is the implementation of the {@link ServerLifeCycleService}
 */
public class ServerLifeCycleServiceImpl implements ServerLifeCycleService {

	/**
	 * Google Guice {@link Injector}
	 */
	@Inject
	private Injector injector;

	/*
	 * @see ServerLifeCycleService#startup(Class)
	 */
	@Override
	public void startup(Class<? extends ServerLifecycleListener> serverListener) throws ServerInitializationException {
		injector.getInstance(serverListener).startup();
	}

	/*
	 * @see ServerLifeCycleService#shutdown(Class)
	 */
	@Override
	public void shutdown(Class<? extends ServerLifecycleListener> serverListener) {
		injector.getInstance(serverListener).shutdown();
	}
}