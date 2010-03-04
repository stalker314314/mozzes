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
package org.mozzes.remoting.client.core;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.mozzes.remoting.client.RemotingClient;
import org.mozzes.remoting.client.RemotingExecutorProviderFactory;
import org.mozzes.remoting.common.RemotingAction;
import org.mozzes.remoting.common.RemotingConfiguration;
import org.mozzes.remoting.common.RemotingException;
import org.mozzes.remoting.common.RemotingProtocol;
import org.mozzes.remoting.common.RemotingResponse;


/**
 * Implementation of remoting client. This client, when provided with configuration connects to remoting server and can
 * execute action on it. Connection is through sockets. Disconnect must be done when client is no longer needed. General
 * pattern for using this client is:
 * 
 * <pre>
 * RemotingClient client = null;
 * try{
 * 	client = new RemotingClientImpl(new RemotingConfiguration(&quot;www.example.com&quot;, 6677));
 * 	client.connect();
 * 	client.execute(...);
 * }finally{
 * 	if (client != null)
 * 		client.disconnect();
 * }
 * </pre>
 * 
 * <p>
 * For easier access to remoting server, look at {@link RemotingExecutorProviderFactory} which should be central place
 * for creating and using remoting clients
 * </p>
 * 
 * @author Perica Milosevic
 * @author Kokan
 */
public class RemotingClientImpl implements RemotingClient {

	/** Socket to communicate with remoting server */
	private Socket clientSocket = null;

	/** Communication protocol */
	private RemotingProtocol remotingProtocol = null;

	/** Are we connected with remoting server */
	private boolean connectionEstablished = false;

	/** Client configuration needed for connecting */
	private final RemotingConfiguration configuration;

	RemotingClientImpl(RemotingConfiguration configuration) {
		this.configuration = configuration;
	}

	/**
	 * Constructor that takes remoting server address and port
	 * 
	 * @param address Address of remoting server
	 * @param port Remoting server port
	 */
	RemotingClientImpl(String address, int port) {
		this.configuration = new RemotingConfiguration(address, port);
	}

	@Override
	public synchronized void connect() throws RemotingException {
		if (!connectionEstablished) {
			try {
				clientSocket = new Socket(configuration.getHost(), configuration.getPort());
				clientSocket.setSoTimeout(0);

				remotingProtocol = RemotingProtocol.buildClientSide(clientSocket, false, false);

				connectionEstablished = true;

			} catch (UnknownHostException ex) {
				throw new RemotingException("Unknown host: " + configuration.getHost(), ex);
			} catch (IOException ex) {
				throw new RemotingException("Connecting unsuccessful with remoting server!", ex);
			}
		}
	}

	@Override
	public synchronized void disconnect() {
		if (connectionEstablished) {
			connectionEstablished = false;

			if (remotingProtocol != null) 
				remotingProtocol.close();

			if (clientSocket != null) {
				try {
					clientSocket.close();
				} catch (IOException ex) {
				}
				clientSocket = null;
			}
		}
	}

	@Override
	public synchronized boolean isConnected() {
		return connectionEstablished;
	}

	@Override
	public synchronized RemotingResponse execute(RemotingAction action) throws RemotingException {

		if (!connectionEstablished)
			throw new RemotingException("Connection with remoting server is not established!");

		try {
			remotingProtocol.send(action);
		} catch (IOException ex) {
			throw new RemotingException(ex);
		}

		return receiveResponse();
	}

	private RemotingResponse receiveResponse() throws RemotingException {
		Object serverResponse;
		try {
			serverResponse = remotingProtocol.receive();
		} catch (IOException ex) {
			throw new RemotingException(ex);
		}

		/* we got RemotingResponse as response */
		if (serverResponse instanceof RemotingResponse)
			return (RemotingResponse) serverResponse;

		// we got RemotingException as response */
		else if (serverResponse instanceof RemotingException)
			throw (RemotingException) serverResponse;

		/* unknown response from remoting server */
		else
			throw new RemotingException("Unknown reply from remoting server! (" + serverResponse + ")");
	}
}
