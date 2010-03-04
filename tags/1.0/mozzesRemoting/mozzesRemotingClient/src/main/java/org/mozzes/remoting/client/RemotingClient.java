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
