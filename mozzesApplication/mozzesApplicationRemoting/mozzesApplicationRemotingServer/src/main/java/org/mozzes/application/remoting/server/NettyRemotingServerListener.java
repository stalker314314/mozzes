package org.mozzes.application.remoting.server;

import java.io.IOException;

import org.mozzes.application.module.ServerInitializationException;
import org.mozzes.application.module.ServerLifecycleListener;
import org.mozzes.remoting.server.RemotingServer;
import org.mozzes.remoting.server.RemotingServerFactory;
import org.mozzes.remoting.server.netty.NettyServerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * Listener which interact based upon received event. It starts or shuts down netty server.
 * 
 * @author Marko Jovicic <marko.jovicic@mozzartbet.com>
 * @author Vladimir Todorovic
 */
public class NettyRemotingServerListener implements ServerLifecycleListener {

  private static final Logger logger = LoggerFactory.getLogger(NettyRemotingServerListener.class);

  @Inject
  private NettyServerConfiguration nettyServerConfiguration;

  @Inject
  private Injector injector;

  @Override
  public void startup() throws ServerInitializationException {
    InvocationActionMapping mappings = injector.getInstance(InvocationActionMapping.class);

    RemotingServer nettyRemotingServer = RemotingServerFactory.getNettyServer(nettyServerConfiguration);
    nettyRemotingServer.addActionMapping(mappings);

    try {
      nettyRemotingServer.startServer();
    } catch (IOException e) {
      throw new ServerInitializationException("Unable to start Netty remoting server", e);
    }
  }

  @Override
  public void shutdown() {
    RemotingServerFactory.getNettyServer(nettyServerConfiguration).stopServer();
    logger.info("Netty remoting server stopped");
  }

}
