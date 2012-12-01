package org.mozzes.remoting.server.netty;

import java.net.SocketAddress;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.mozzes.remoting.common.netty.ClientIdentificationMessage;

/**
 * Handles identification for connected clients.
 * 
 * @author Vladimir Todorovic
 */
public final class IdentificationHandler extends SimpleChannelUpstreamHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(IdentificationHandler.class);
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		if (e.getMessage() instanceof ClientIdentificationMessage) {
			ClientIdentificationMessage identificationMessage = (ClientIdentificationMessage) e.getMessage();
			
			SocketAddress remoteAddress = ctx.getChannel().getRemoteAddress();
			Integer clientId = identificationMessage.getClientId();
			
			if (NettyRemotingServer.connectedClients.addIfAbsent(clientId)) {
				ctx.getChannel().setAttachment(new ClientIdentity(clientId, true));
				logger.info("Remote client is accepted [Remote address = " + remoteAddress + ", Client ID = " + clientId + "]");
			} else {
				ctx.getChannel().setAttachment(new ClientIdentity(clientId, false));
				logger.info("Remote client is not accepted [Remote address = " + remoteAddress + ", Client ID = " + clientId + "]");
			}
			
			ctx.getPipeline().remove(this);
			
			return;
		}
		
		super.messageReceived(ctx, e);
	}
}
