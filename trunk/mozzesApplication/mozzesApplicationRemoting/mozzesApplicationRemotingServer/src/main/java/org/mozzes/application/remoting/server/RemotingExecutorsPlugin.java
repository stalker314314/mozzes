package org.mozzes.application.remoting.server;

import org.mozzes.application.plugin.ApplicationPlugin;

import com.google.inject.Binder;
import com.google.inject.Scopes;

/**
 * Binds invocations needed for client remoting calls. Clients need them to invoke their requests/actions on the server
 * side.
 * 
 * @author Marko Jovicic <marko.jovicic@mozzartbet.com>
 * 
 */
public class RemotingExecutorsPlugin extends ApplicationPlugin {

  @Override
  public void doCustomBinding(Binder binder) {
    super.doCustomBinding(binder);
    binder.bind(InvocationActionExecutor.class).in(Scopes.SINGLETON);
    binder.bind(InvocationActionMapping.class).in(Scopes.SINGLETON);
    binder.bind(InvocationExecutorProvider.class).in(Scopes.SINGLETON);
  }
}
