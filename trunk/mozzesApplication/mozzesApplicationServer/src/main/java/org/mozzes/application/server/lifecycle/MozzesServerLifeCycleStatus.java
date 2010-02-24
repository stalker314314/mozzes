package org.mozzes.application.server.lifecycle;

/**
 * This class represents the status of the mozzes server(if started is true that means that initialization of the server
 * is finished).
 * 
 * @author vita
 */
public class MozzesServerLifeCycleStatus {

	private boolean started = false;

	public void setStarted() {
		started = true;
	}

	public boolean isStarted() {
		return started;
	}
}
