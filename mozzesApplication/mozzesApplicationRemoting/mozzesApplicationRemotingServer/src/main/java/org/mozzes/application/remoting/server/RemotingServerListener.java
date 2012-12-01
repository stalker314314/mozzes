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
package org.mozzes.application.remoting.server;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.mozzes.application.module.ServerInitializationException;
import org.mozzes.application.module.ServerLifecycleListener;
import org.mozzes.remoting.server.RemotingServer;
import org.mozzes.remoting.server.RemotingServerFactory;

/**
 * Listener that waits on the socket for remote invocations that are actually remote invocations of server's services.
 */
public class RemotingServerListener implements ServerLifecycleListener {

	private static final Logger logger = LoggerFactory.getLogger(RemotingServerListener.class);

	/** The server port. */
	@Inject
	@RemotingServerPort
	private int serverPort;

	/** The injector. */
	@Inject
	private Injector injector;

	/*
	 * When the Mozzes server is started fire up the remoting server for receiving remoting actions that are actually
	 * transporting the invocations of the server's services
	 * 
	 * @see ServerListener#serverStarted()
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mozzes.application.module.ServerListener#serverStarted()
	 */
	@Override
	public void startup() throws ServerInitializationException {
		InvocationActionMapping mappings = injector.getInstance(InvocationActionMapping.class);
		
		RemotingServer remotingServer = RemotingServerFactory.getServer(serverPort);
		remotingServer.addActionMapping(mappings);
		
		try {
			remotingServer.startServer();
			logger.info("Remoting server started on port " + serverPort);
		} catch (IOException e) {
			throw new ServerInitializationException("Unable to start remoting server", e);
		}
	}

	/*
	 * When Mozzes server is going down remoting server is going to kill himself
	 * 
	 * @see ServerListener#serverStopped()
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mozzes.application.module.ServerListener#serverStopped()
	 */
	@Override
	public void shutdown() {
		RemotingServerFactory.getServer(serverPort).stopServer();
		logger.info("Mozzes remoting server stopped");
	}
}