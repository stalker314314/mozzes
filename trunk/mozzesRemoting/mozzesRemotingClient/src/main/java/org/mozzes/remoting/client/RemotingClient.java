package org.mozzes.remoting.client;

import org.mozzes.remoting.common.RemotingActionExecutor;
import org.mozzes.remoting.common.RemotingException;

/**
 * Interface that all remoting clients must implement. This interface provides connecting, disconnecting and querying
 * state of connection with remoting server.
 * 
 * @author Perica Milosevic
 * @author Kokan
 */
public interface RemotingClient extends RemotingActionExecutor {

	/**
	 * Connects with remoting server
	 * 
	 * @throws RemotingException If remoting server is not available or any network problem while trying to establish
	 *             connection
	 */
	public void connect() throws RemotingException;

	/**
	 * Disconnects from remoting server. If connection was not established, should not do anything. Any stream and
	 * socket must be closed after this method is executed
	 */
	public void disconnect();

	/**
	 * Query the connection state
	 * 
	 * @return <code>true</code> if connection is established, <code>false</code> otherwise
	 */
	public boolean isConnected();
}
