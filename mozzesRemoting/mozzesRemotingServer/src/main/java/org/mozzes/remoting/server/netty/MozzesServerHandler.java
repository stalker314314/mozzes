package org.mozzes.remoting.server.netty;

import java.net.InetAddress;
import java.util.HashMap;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.mozzes.remoting.server.RemotingActionDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.mozzes.remoting.common.RemotingAction;
import org.mozzes.remoting.common.RemotingActionExecutor;
import org.mozzes.remoting.common.RemotingException;
import org.mozzes.remoting.common.RemotingResponse;

/**
 * This handler executes remoting actions on Mozzes server.
 * 
 * @author Bojan Blagojevic <bojan.blagojevic@mozzartbet.com>
 * @author Marko Jovicic <marko.jovicic@mozzartbet.com>
 * @author Vladimir Todorovic
 */
public class MozzesServerHandler extends SimpleChannelUpstreamHandler {

	private static final Logger logger = LoggerFactory.getLogger(MozzesServerHandler.class);

	private final RemotingActionDispatcher dispatcher;

	/**
	 * Creates handler that executes remoting actions on Mozzes server.
	 * 
	 * @param dispatcher - dispatches remoting action to the action executor
	 */
	public MozzesServerHandler(RemotingActionDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		// HERE: Add all accepted channels to the group
		// so that they are closed properly on shutdown
		// If the added channel is closed before shutdown,
		// it will be removed from the group automatically.
		NettyRemotingServer.clientChannelGroup.add(ctx.getChannel());
		super.channelOpen(ctx, e);
	}

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		if (e instanceof ChannelStateEvent) {
			logger.info(e.toString());
		}
		super.handleUpstream(ctx, e);
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		logger.info("Client {} connected.", InetAddress.getLocalHost().getHostName());
		super.channelConnected(ctx, e);
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		// ako ih dodajemo onda ih i brisemo.
		NettyRemotingServer.clientChannelGroup.remove(e.getChannel());
		super.channelDisconnected(ctx, e);
		
		Object channelAttachment = ctx.getChannel().getAttachment();
		
		if (channelAttachment != null) {
			ClientIdentity clientIdentity = (ClientIdentity) channelAttachment;
			
			if (clientIdentity.isAccepted()) {
				NettyRemotingServer.connectedClients.remove(clientIdentity.getClientId());
			}
		}
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent messageEvent) {
		long started = System.currentTimeMillis();	
		
		RemotingAction remoteAction = (RemotingAction) messageEvent.getMessage();
		Long requestId = remoteAction.getId();
		
		try {
			Object channelAttachment = ctx.getChannel().getAttachment();
			
			if (channelAttachment != null) {
				ClientIdentity clientIdentity = (ClientIdentity) channelAttachment;
				
				if (!clientIdentity.isAccepted()) {
					logger.error("Client {} is already in use.", clientIdentity.getClientId());
					throw new RemotingException(new IllegalStateException(
							"Client ID is already in use. Client ID: " + clientIdentity.getClientId()));
				}
			}
				
			RemotingResponse remotingResponse = execute(remoteAction);			
			remotingResponse.setId(requestId);			
		
			logger.debug("processAction() before sending result");			
			messageEvent.getChannel().write(remotingResponse);
			logger.debug("processAction() after sending result");
		} catch (ClassCastException ex) {
			logger.error("Unknown class received from client", ex);	
			
			RemotingException exception = new RemotingException(ex);
			exception.setId(requestId);
			messageEvent.getChannel().write(exception);	
			
			logger.debug("processAction() after sending class cast exception");
		} catch (RemotingException exception) {
			// doslo je do greske prilikom izvrsavanja akcije
			logger.debug("processAction() before sending remoting exception", exception);
			exception.setId(requestId);
			messageEvent.getChannel().write(exception);
			logger.debug("processAction() after sending remoting exception");
		}
		
		logger.debug("request processing took {} ms.", (System.currentTimeMillis() - started));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		logger.error("Unexpected exception from downstream.", e.getCause());
		logger.info("exceptionCaught, num of clients in group is " + NettyRemotingServer.clientChannelGroup.size());
	}

	/**
	 * Execute remote action logic.
	 * 
	 * @param request request that came from client.
	 * @return action execution response.
	 * @throws RemotingException if action execution fails, exception is thrown to indicate that error.
	 */
	private RemotingResponse execute(RemotingAction request) throws RemotingException {
		logger.debug("Request received: {}", request);

		RemotingActionExecutor actionExecutor = dispatcher.getActionExecutor(request);

		// izvrsavanje akcije
		RemotingResponse response = null;
		try {
			logger.debug("processAction() before executing action");
			response = actionExecutor.execute(request);
		} catch (RemotingException ex) {
			throw ex;
		} catch (Throwable thr) {
			throw new RemotingException(thr);
		}

		// akcija mora da vrati neki response klijentu
		if (response == null) {
			response = new RemotingResponse(new HashMap<Object, Object>());
		}

		return response;
	}

}
