package org.mozzes.remoting.client.netty;

import org.mozzes.remoting.client.RemotingClient;

import org.mozzes.remoting.common.RemotingActionExecutor;
import org.mozzes.remoting.common.RemotingActionExecutorProvider;
import org.mozzes.remoting.common.RemotingException;

/**
 * Simple client provider that holds only one client and connects it at the startup. Ideal for uses where only one
 * client is needed and we don't want a pool.
 * 
 * @author Bojan Blagojevic <bojan.blagojevic@mozzartbet.com>
 * @author Vladimir Todorovic
 */
public class NettyRemotingClientProvider implements RemotingActionExecutorProvider {

  private final RemotingClient client;

  /**
   * Creates provader for Netty clients.
   * 
   * @throws RemotingException
   *           if remoting server is not available or any network problem while trying to establish connection
   */
  public NettyRemotingClientProvider(NettyRemotingClient nettyRemotingClient) throws RemotingException {
    this.client = nettyRemotingClient;
    this.client.connect();
  }

  @Override
  public RemotingActionExecutor get() {
    return client;
  }

}
