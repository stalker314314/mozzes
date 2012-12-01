package org.mozzes.remoting.server.netty;

import java.util.concurrent.Callable;

import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represent one response.
 * @author Marko Jovicic <marko.jovicic@mozzartbet.com>
 *
 */
public class OneResponse implements Callable<Void> {
	
	private static final Logger logger = LoggerFactory.getLogger(OneResponse.class);
	
	private Channel channel;
	private Object response;

	/**
	 * Associate response with a channel.
	 * @param channel communication channel to the client.
	 * @param response response to send to the client.
	 */
	public OneResponse(Channel channel, Object response) {
		this.channel = channel;
		this.response = response;
		logger.debug("OneResponse object created and it is ready to be returned to client.");
	}

	@Override
	public Void call() throws Exception {
		logger.info("writing response STARTED to the client.");
		long started = System.currentTimeMillis();
		
		channel.write(response).awaitUninterruptibly();		
		
		logger.debug("writing response took: " + (System.currentTimeMillis() - started) + " ms");
		return null;
	}

}
