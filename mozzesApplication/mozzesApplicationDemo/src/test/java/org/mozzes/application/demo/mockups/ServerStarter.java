package org.mozzes.application.demo.mockups;

import org.mozzes.application.server.MozzesServer;

public class ServerStarter extends Thread {

	private final MozzesServer server;

	public ServerStarter(MozzesServer server) {
		this.server = server;
	}

	@Override
	public void run() {
		server.start();
	}

}
