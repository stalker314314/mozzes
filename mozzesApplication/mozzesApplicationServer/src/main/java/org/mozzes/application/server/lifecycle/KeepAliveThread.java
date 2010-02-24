package org.mozzes.application.server.lifecycle;

/**
 * Thread that keeps server alive
 */
class KeepAliveThread  extends Thread {
	
	// 100 years :)
	private static final long MAX_ALIVE_TIME = 1000 * 60 * 60 * 24 * 365 * 100;

	private volatile boolean alive = true;
	
	KeepAliveThread() {
		super("KeepAliveThread");
		setPriority(MIN_PRIORITY);
		setDaemon(false);
	}
	
	@Override
	public void run() {
		while (alive) {
			try {
				Thread.sleep(MAX_ALIVE_TIME);
			} catch (InterruptedException e) {
				interrupted();
			}
		}
	}
	
	synchronized boolean die() {
		if (!alive)
			return false;
		
		alive = false;
		interrupt();
		return true;
	}
}