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
	public void startup() throws ServerInitializationException {
		MozzesGuiceProviderFactory.setGuiceProvider(guiceInjector);
		
		final Map<String, String> initParams = new HashMap<String, String>();
		initParams.put("com.sun.jersey.config.property.packages", configuration.getRootResourcePackage());

		logger.info("Starting grizzly on " + configuration.getBaseUri());
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
	
	@Override
	public void shutdown() {
		threadSelector.stopEndpoint();
	}
}