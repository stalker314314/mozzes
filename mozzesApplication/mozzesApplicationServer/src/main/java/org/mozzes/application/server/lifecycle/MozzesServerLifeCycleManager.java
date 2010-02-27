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
package org.mozzes.application.server.lifecycle;

import java.util.ArrayList;
import java.util.List;

import org.mozzes.application.common.client.MozzesClient;
import org.mozzes.application.module.ServerInitializationException;
import org.mozzes.application.module.ServerLifecycleListener;
import org.mozzes.application.server.service.ServerLifeCycleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Server life cycle manager, performs tasks during server startup and shutdown and keeps server alive
 */
public class MozzesServerLifeCycleManager {

	private static final Logger logger = LoggerFactory.getLogger(MozzesServerLifeCycleManager.class);

	/**
	 * List of listeners subscribed to get server life cycle events
	 */
	private final List<Class<? extends ServerLifecycleListener>> serverListeners = new ArrayList<Class<? extends ServerLifecycleListener>>();

	/**
	 * Thread that keeps server alive
	 */
	private final KeepAliveThread keepAliveThread = new KeepAliveThread();

	/**
	 * Is server started?
	 */
	private boolean started = false;

	public void addServerListeners(List<Class<? extends ServerLifecycleListener>> listeners) {
		if (listeners != null)
			this.serverListeners.addAll(listeners);
	}

	/**
	 * This method starts the server and informs all the listeners about server startup
	 */
	public synchronized void start(MozzesClient client) throws ServerInitializationException {
		if (started)
			return;// TODO: maybe throw RuntimeException if start() is called twice

		List<Class<? extends ServerLifecycleListener>> startedListeners = new ArrayList<Class<? extends ServerLifecycleListener>>();
		ServerLifeCycleService lifeCycleService = client.getService(ServerLifeCycleService.class);

		for (Class<? extends ServerLifecycleListener> serverListener : serverListeners)
			try {
				lifeCycleService.startup(serverListener);
				startedListeners.add(serverListener);

			} catch (ServerInitializationException ex) {
				stopListeners(startedListeners, lifeCycleService);
				throw ex;
			} catch (Throwable thr) {
				stopListeners(startedListeners, lifeCycleService);
				throw new ServerInitializationException(thr);
			}

		keepAliveThread.start();
		started = true;
	}

	/**
	 * Stops the mozzes server and all the listeners
	 */
	public synchronized void stop(MozzesClient client) {
		if (!keepAliveThread.die())
			return;

		stopListeners(serverListeners, client.getService(ServerLifeCycleService.class));
	}

	/**
	 * This method stops all the listeners in the provided list by calling
	 * {@link ServerLifeCycleService#shutdown(Class)} method
	 * 
	 * @param startedListeners list of all the listeners that are successfully started
	 * @param lifecycleService service that's shutting down the listeners
	 */
	private void stopListeners(List<Class<? extends ServerLifecycleListener>> startedListeners,
			ServerLifeCycleService lifecycleService) {
		for (Class<? extends ServerLifecycleListener> serverListener : startedListeners)
			try {
				lifecycleService.shutdown(serverListener);
			} catch (Throwable thr) {
				logger.error("Error during server listener shutdown", thr);
			}
	}
}
