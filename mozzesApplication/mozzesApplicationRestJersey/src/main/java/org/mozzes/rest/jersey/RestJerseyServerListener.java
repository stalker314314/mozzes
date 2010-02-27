package org.mozzes.rest.jersey;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.mozzes.application.module.ServerInitializationException;
import org.mozzes.application.module.ServerLifecycleListener;
import org.mozzes.rest.jersey.guice.MozzesGuiceProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;

/**
 * Simple Mozzes server listener whose purpose is to start Grizzly embedded server and configure Jersey
 * @author stalker
 */
public class RestJerseyServerListener implements ServerLifecycleListener {

	private static final Logger logger = LoggerFactory.getLogger(RestJerseyServerListener.class);

	@Inject
	private RestJerseyConfiguration configuration;
	@Inject
	private Injector guiceInjector;
	
	private SelectorThread threadSelector;

	@Override
	public void shutdown() {
		threadSelector.stopEndpoint();
	}

	@Override
	public void startup() throws ServerInitializationException {
		MozzesGuiceProviderFactory.setGuiceProvider(guiceInjector);
		
		final Map<String, String> initParams = new HashMap<String, String>();
		initParams.put("com.sun.jersey.config.property.packages", configuration.getRootResourcePackage());

		logger.info("Starting grizzly...");
		try {
			threadSelector = GrizzlyWebContainerFactory.create(configuration.getBaseUri(), initParams);
		} catch (IllegalArgumentException e) {
			logger.error("Error starting grizly", e);
			throw new ServerInitializationException(e);
		} catch (IOException e) {
			logger.error("Error starting grizly", e);
			throw new ServerInitializationException(e);
		}
	}
}