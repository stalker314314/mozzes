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