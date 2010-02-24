package org.mozzes.application.remoting.server;

import java.io.IOException;

import org.mozzes.application.module.ServerInitializationException;
import org.mozzes.application.module.ServerLifecycleListener;
import org.mozzes.remoting.server.RemotingServer;
import org.mozzes.remoting.server.RemotingServerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Injector;

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
	 * @see org.mozzes.application.module.ServerListener#serverStarted()
	 */
	@Override
	public void startup() throws ServerInitializationException {
		RemotingServer remotingServer = RemotingServerFactory.getServer(serverPort);
		remotingServer.addActionMapping(injector.getInstance(InvocationActionMapping.class));
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
	 * @see org.mozzes.application.module.ServerListener#serverStopped()
	 */
	@Override
	public void shutdown() {
		RemotingServerFactory.getServer(serverPort).stopServer();
		logger.info("Mozzes remoting server stopped");
	}
}