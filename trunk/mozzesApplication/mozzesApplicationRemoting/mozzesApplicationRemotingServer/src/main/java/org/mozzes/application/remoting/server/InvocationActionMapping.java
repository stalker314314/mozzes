package org.mozzes.application.remoting.server;

import static org.mozzes.application.remoting.common.RemoteInvocationConstants.KEY_ACTION_NAME;

import org.mozzes.remoting.common.RemotingActionExecutor;
import org.mozzes.remoting.server.RemotingActionMapping;

import com.google.inject.Inject;

/**
 * The Class InvocationActionMapping maps is required to specify mapping between logical action name and action
 * {@link RemotingActionExecutor}.<br>
 * It's doing that by mapping action name and {@link RemotingActionExecutor} factory.
 * 
 * @see RemotingActionMapping
 */
class InvocationActionMapping extends RemotingActionMapping {

	/**
	 * Instantiates a new invocation action mapping and adds mapping for remote method invocation calls
	 */
	@Inject
	public InvocationActionMapping(InvocationExecutorProvider executorFactory) {
		super(KEY_ACTION_NAME, executorFactory);
	}
}
