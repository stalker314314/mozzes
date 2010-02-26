package org.mozzes.application.remoting.server;

import java.util.ArrayList;
import java.util.List;

import org.mozzes.application.module.ServerLifecycleListener;
import org.mozzes.application.plugin.ApplicationPlugin;

import com.google.inject.Binder;
import com.google.inject.Scopes;

/**
 * Module that adds remoting support to the Mozzes server so clients can connect via the mozzes remoting to the mozzes
 * application server and execute service invocations.
 */
public class RemotingPlugin extends ApplicationPlugin {

	/** Port on which server accepts connections. */
	private final int serverPort;

	/**
	 * Instantiates a new application remoting module.
	 */
	public RemotingPlugin(int serverPort) {
		if (serverPort < 1)
			throw new IllegalArgumentException("Illegal server port");

		this.serverPort = serverPort;
	}

	/*
	 * Here we provide the binding that is needed to accept remote service calls
	 * 
	 * @see ApplicationModule#doCustomBinding(Binder)
	 */
	@Override
	public void doCustomBinding(Binder binder) {
		binder.bind(InvocationActionExecutor.class).in(Scopes.SINGLETON);
		binder.bind(InvocationActionMapping.class).in(Scopes.SINGLETON);
		binder.bind(InvocationExecutorProvider.class).in(Scopes.SINGLETON);
		binder.bind(int.class).annotatedWith(RemotingServerPort.class).toInstance(Integer.valueOf(serverPort));
	}

	/*
	 * Create new listener for the remote actions(that contains remove service invocations)
	 * 
	 * @see ApplicationModule#getServerListeners()
	 */
	@Override
	public List<Class<? extends ServerLifecycleListener>> getServerListeners() {
		List<Class<? extends ServerLifecycleListener>> returnValue = new ArrayList<Class<? extends ServerLifecycleListener>>();
		returnValue.add(RemotingServerListener.class);
		return returnValue;
	}
}