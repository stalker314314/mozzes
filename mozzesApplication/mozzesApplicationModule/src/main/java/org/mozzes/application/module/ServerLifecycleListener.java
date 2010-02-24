package org.mozzes.application.module;

/**
 * Custom modules that are plugged in the Mozzes Server can be subscribed to the events about starting and stopping the
 * server and do some custom actions.
 */
public interface ServerLifecycleListener {

	/**
	 * This method is called when the server is started
	 */
	public void startup() throws ServerInitializationException;

	/**
	 * This method is called when the server is stopped
	 */
	public void shutdown();
}
