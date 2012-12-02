package org.mozzes.remoting.server.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import org.mozzes.remoting.server.RemotingServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This remoting server uses Netty for remoting.
 */
public class NettyRemotingServer extends RemotingServer {

  private static final Logger logger = LoggerFactory.getLogger(NettyRemotingServer.class);

  /**
   * All clients connected to the server are contained inhere. So, when someone stop server, all these clients will be
   * in once detached from the server.
   */
  static final ChannelGroup clientChannelGroup = new DefaultChannelGroup("clients");

  static final ClientGroup connectedClients = new ClientGroup();

  // Configure the server.
  private NioServerSocketChannelFactory channelFactory;
  private ServerBootstrap bootstrap;
  private ExecutionHandler executionHandler;

  private final NettyServerConfiguration nettyServerConfiguration;

  /**
   * 
   * @param nettyServerConfiguration
   *          - configuration for Netty server
   */
  public NettyRemotingServer(NettyServerConfiguration nettyServerConfiguration) {
    super(nettyServerConfiguration.getNettyPort());

    this.nettyServerConfiguration = nettyServerConfiguration;
  }

  @Override
  public synchronized void startServer() {
    if (bootstrap != null) {
      return;
    }

    channelFactory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),
        Executors.newCachedThreadPool(), nettyServerConfiguration.getIoworkers());

    bootstrap = new ServerBootstrap(channelFactory);

    executionHandler = new ExecutionHandler(new OrderedMemoryAwareThreadPoolExecutor(
        nettyServerConfiguration.getWorkers(), nettyServerConfiguration.getMemoryPerChannel(),
        nettyServerConfiguration.getMemoryPerPool()));

    ChannelPipelineFactory pipelineFactory = new MozzesServerPipelineFactory(getDispatcher(), executionHandler);
    bootstrap.setPipelineFactory(pipelineFactory);

    bootstrap.setOption("child.tcpNoDelay", true);
    bootstrap.setOption("child.keepAlive", true);

    clientChannelGroup.add(bootstrap.bind(new InetSocketAddress(getPort())));

    logger.info(nettyServerConfiguration.toString());
  }

  @Override
  public synchronized void stopServer() {
    if (bootstrap == null) {
      logger.info("Server is already down.");
      return;
    }

    logger.info("Shutting down server");
    // HERE: Close all connections and server sockets.
    clientChannelGroup.close().awaitUninterruptibly();

    // HERE: Shutdown the selector loop (boss and worker).
    channelFactory.releaseExternalResources();// <-- HERE
    executionHandler.releaseExternalResources();

    bootstrap = null;
  }

  @Override
  public synchronized boolean isRunning() {
    return bootstrap != null;
  }

  @Override
  public synchronized int getNumberOfActiveClients() {
    return clientChannelGroup.size();
  }
}
