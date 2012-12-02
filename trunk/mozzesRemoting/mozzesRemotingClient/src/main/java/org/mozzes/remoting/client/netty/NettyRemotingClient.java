package org.mozzes.remoting.client.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.mozzes.remoting.client.RemotingClient;

import org.mozzes.remoting.common.RemotingAction;
import org.mozzes.remoting.common.RemotingClientListener;
import org.mozzes.remoting.common.RemotingConfiguration;
import org.mozzes.remoting.common.RemotingException;
import org.mozzes.remoting.common.RemotingResponse;
import org.mozzes.remoting.common.netty.ClientIdentificationMessage;
import org.mozzes.remoting.common.netty.FutureAction;

/**
 * Remoting client which sits on Netty NIO client. Using Netty mechanism client dispatch remote actions to the server
 * for execution. Simple spoken, a client is responsible for connecting, disconnecting and executing remote actions.
 * 
 * 
 * @author Bojan Blagojevic <bojan.blagojevic@mozzartbet.com>
 * @author Marko Jovicic <marko.jovicic@mozzartbet.com>
 * @author Vladimir Todorovic
 */
public class NettyRemotingClient extends RemotingClientListener implements RemotingClient {

  private static final boolean NEW_SERVER = true;
  private ClientBootstrap bootstrap;
  private Channel channel;

  /**
   * Unique id for each remoting action.
   */
  private AtomicLong uniqeId = new AtomicLong();

  /**
   * Service to dispatch client remote actions to the server. It has <strong>>one</strong> worker so it ensures it can
   * not happen simultaneous write on a single communication channel.
   */
  private ExecutorService service = Executors.newFixedThreadPool(1);

  /**
   * It allows that one client can send more remote actions on execution, so it doesn't have to wait on a single action
   * response. This is a map which keeps track which action correspond to which respond.
   */
  private ConcurrentHashMap<Long, FutureAction> synchronizedRequestsAndResponses = new ConcurrentHashMap<Long, FutureAction>();

  /**
   * Create Netty remoting client based on this configuration.
   * 
   * @param configuration
   *          - remoting configuration used to create stuff for the client. Also, it has parameters for the connection.
   */
  public NettyRemotingClient(RemotingConfiguration configuration) {
    // Configure the client.
    bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(),
        Executors.newCachedThreadPool()));

    MozzesClientHandler handler = new MozzesClientHandler(synchronizedRequestsAndResponses);
    bootstrap.setOption("remoteAddress", new InetSocketAddress(configuration.getHost(), configuration.getPort()));
    bootstrap.setPipelineFactory(new MozzesClientPipelineFactory(handler, NEW_SERVER));

    bootstrap.setOption("tcpNoDelay", true);
    bootstrap.setOption("keepAlive", true);
  }

  @Override
  public synchronized RemotingResponse execute(RemotingAction action) throws RemotingException {
    // this is blocking sync execution
    Object serverResponse;
    try {
      serverResponse = executeAsync(action).get();
    } catch (Exception exception) {
      throw new RemotingException(exception);
    }

    if (serverResponse instanceof RemotingResponse) {
      return (RemotingResponse) serverResponse;
    } else if (serverResponse instanceof RemotingException) {
      throw (RemotingException) serverResponse;
    } else {
      throw new RemotingException("Unknown reply from remoting server! (" + serverResponse + ")");
    }
  }

  /**
   * Asynchronous execution of the action.
   * 
   * @param action
   *          action to be executed remotely
   * @return {@link Future} is returned holding future result.
   * @throws RemotingException
   *           error while executing action.
   */
  private Future<Object> executeAsync(RemotingAction action) throws RemotingException {
    if (!channel.isConnected()) {
      throw new RemotingException("Communication channel is unreacheable! Can not send execution request.");
    }

    action.setId(getNextUniqueId());

    FutureAction responceAsync = new FutureAction(channel, action);
    synchronizedRequestsAndResponses.put(action.getId(), responceAsync);
    return service.submit(responceAsync);
  }

  /**
   * Generate next unique id.
   * 
   * @return next unique id
   */
  private Long getNextUniqueId() {
    return uniqeId.incrementAndGet();
  }

  @Override
  public void connect() throws RemotingException {
    // Start the connection attempt.
    ChannelFuture channelFuture = bootstrap.connect();
    channel = channelFuture.awaitUninterruptibly().getChannel();

    if (!channel.isConnected()) {
      disconnect();
      throw new RemotingException("not connected yet.");
    }
  }

  @Override
  public void disconnect() {
    // Close the connection. Make sure the close operation ends because
    // all I/O operations are asynchronous in Netty.
    channel.close().awaitUninterruptibly();

    // Shut down all thread pools to exit.
    bootstrap.releaseExternalResources();
  }

  @Override
  public boolean isConnected() {
    return channel == null ? false : channel.isConnected();
  }

  @Override
  public void clientIsConnected(Integer clientId) {
    channel.write(new ClientIdentificationMessage(clientId)).awaitUninterruptibly();
  }

  @Override
  public void loginClientFailed() {
    disconnect();
  }

  @Override
  public void clientLogout() {
    disconnect();
  }
}
