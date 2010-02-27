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
package org.mozzes.remoting.client.pool;

import java.net.ServerSocket;
import java.net.Socket;

import org.mozzes.remoting.common.RemotingProtocol;
import org.mozzes.remoting.common.RemotingResponse;


/**
 * Simple mockup server that acts like a normal server, always returning empty {@link RemotingResponse}.
 * 
 * Server is configured with port and number of objects it will receive (in constructor). Later, received objects can be
 * examined with {@link #getLastSentObject()} and {@link #getLastError()} methods.
 * 
 * @author Kokan
 */
public class MockupServer extends Thread {

	private final int port;
	private int numReceive;

	private Object lastSentObject;
	private Throwable lastError = null;

	public MockupServer(int numReceive, int port) {
		this.port = port;
		this.numReceive = numReceive;
	}

	@Override
	public void run() {
		try {
			ServerSocket ss = new ServerSocket(this.port);
			Socket s = ss.accept();
			RemotingProtocol rp = RemotingProtocol.buildServerSide(s);
			while (numReceive-- > 0) {
				lastSentObject = rp.receive();
				rp.send(new RemotingResponse());
			}
			rp.close();
		} catch (Throwable t) {
			this.lastError = t;
		}
	}

	public Object getLastSentObject() {
		return lastSentObject;
	}

	public Throwable getLastError() {
		return lastError;
	}
}