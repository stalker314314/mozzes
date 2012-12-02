package org.mozzes.application.remoting.server;

import java.util.ArrayList;
import java.util.List;

import org.mozzes.application.module.ServerLifecycleListener;
import org.mozzes.application.plugin.ApplicationPlugin;
import org.mozzes.remoting.server.netty.NettyServerConfiguration;

import com.google.inject.Binder;

/**
 * Netty remoting plugin.
 * 
 * @author Marko Jovicic <marko.jovicic@mozzartbet.com>
 * @author Bojan Blagojevic <bojan.blagojevic@mozzartbet.com>
 * @author Vladimir Todorovic
 */
public class NettyRemotingPlugin extends ApplicationPlugin {

  /** Port on which server accepts connections. */
  private final NettyServerConfiguration nettyServerConfiguration;

  /**
   * Creates plugin for Netty remoting server.
   * 
   * @throws IllegalArgumentException
   *           if server configuration is null and/or server listener is null
   */
  public NettyRemotingPlugin(NettyServerConfiguration nettyServerConfiguration) throws IllegalArgumentException {

    if (nettyServerConfiguration == null) {
      throw new IllegalArgumentException("Netty server configuration can not be null");
    }

    this.nettyServerConfiguration = nettyServerConfiguration;
  }

  /*
   * Here we provide the binding that is needed to accept remote service calls
   * 
   * @see ApplicationModule#doCustomBinding(Binder)
   */
  @Override
  public void doCustomBinding(Binder binder) {
    binder.bind(NettyServerConfiguration.class).toInstance(nettyServerConfiguration);
  }

  /*
   * Create new listener for the remote actions(that contains remove service invocations)
   * 
   * @see ApplicationModule#getServerListeners()
   */
  @Override
  public List<Class<? extends ServerLifecycleListener>> getServerListeners() {
    List<Class<? extends ServerLifecycleListener>> returnValue = new ArrayList<Class<? extends ServerLifecycleListener>>();
    returnValue.add(NettyRemotingServerListener.class);
    return returnValue;
  }
}
