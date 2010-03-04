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