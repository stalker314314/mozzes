package org.mozzes.remoting.client.netty;

import java.util.concurrent.ConcurrentHashMap;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.mozzes.remoting.common.netty.FutureAction;
import org.mozzes.remoting.common.netty.Unique;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handle incoming messages from the server. Also, it keeps a list of client's requests in order to successfully return
 * response to requested client action. In case of lost communication channel with server, all client's requests are
 * notified about this event so they can stop waiting to an answer/response from the server.
 * 
 * @author Bojan Blagojevic <bojan.blagojevic@mozzartbet.com>
 * @author Marko Jovicic <marko.jovicic@mozzartbet.com>
 * 
 */
public class MozzesClientHandler extends SimpleChannelUpstreamHandler {

  private static final Logger logger = LoggerFactory.getLogger(MozzesClientHandler.class);

  private final ConcurrentHashMap<Long, FutureAction> synchronizedRequestsAndResponses;

  /**
   * Creates Mozzes client handler. It is responsible for receiving messages/responses from the server and redirecting
   * those to a proper request. (One response correspond to one request)
   * 
   * @param synchronizedRequestsAndResponses
   *          holds client requests identified by its ID.
   */
  public MozzesClientHandler(ConcurrentHashMap<Long, FutureAction> synchronizedRequestsAndResponses) {
    this.synchronizedRequestsAndResponses = synchronizedRequestsAndResponses;
  }

  @Override
  public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {

    if (e instanceof ChannelStateEvent) {
      logger.debug("[handle upstream] " + e.toString());
    }

    super.handleUpstream(ctx, e);
  }

  @Override
  public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {

    Object message = e.getMessage();
    if (message instanceof Unique) {
      long uniqueId = ((Unique) message).getId();
      synchronizedRequestsAndResponses.remove(uniqueId).setResponse(message);
    } else {
      logger.error("Message received which unknown id. Unexpected messge.");
      throw new Exception("Message received whith unknown id. Unexpected message.");
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
    logger.error("Unexpected exception from downstream.", e.getCause());
    e.getChannel().close();
  }

  @Override
  public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
    sendErrorMessagesForWaitingResponses();
    super.channelClosed(ctx, e);
  }

  /**
   * Send general error message to client's requests who wait for their responses. This is a case when there are clients
   * who haven't yet got their response and when communication with server is lost they have to be notified so they
   * don't wait anymore.
   */
  private void sendErrorMessagesForWaitingResponses() {
    for (FutureAction response : synchronizedRequestsAndResponses.values()) {
      response.setResponse(new Exception("channel closed."));
    }

    synchronizedRequestsAndResponses.clear();
  }

}
