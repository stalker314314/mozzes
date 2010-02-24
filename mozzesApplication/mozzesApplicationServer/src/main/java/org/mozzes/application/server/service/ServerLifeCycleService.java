package org.mozzes.application.server.service;

import org.mozzes.application.module.ServerInitializationException;
import org.mozzes.application.module.ServerLifecycleListener;

/**
 * This is the service that's responsible for informing the provided server listener about server's starting and
 * stopping
 */
public interface ServerLifeCycleService {

	/**
	 * This method is called on the server's startup
	 * 
	 * @param serverListener listener that will be informed about server startup
	 * @throws ServerInitializationException when there's some problem with informing the listener@param serverListener
	 *             list of listeners that will be informed about server startup
	 */
	void startup(Class<? extends ServerLifecycleListener> serverListener) throws ServerInitializationException;

	/**
	 * @param serverListener list of listeners that will be informed about server shutdown
	 */
	void shutdown(Class<? extends ServerLifecycleListener> serverListener);
}
