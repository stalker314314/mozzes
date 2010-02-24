package org.mozzes.application.remoting.client;

import static org.mozzes.application.remoting.common.RemoteInvocationConstants.KEY_ACTION_NAME;
import static org.mozzes.application.remoting.common.RemoteInvocationConstants.KEY_METHOD_INVOCATION;
import static org.mozzes.application.remoting.common.RemoteInvocationConstants.KEY_RESULT;
import static org.mozzes.application.remoting.common.RemoteInvocationConstants.KEY_SESSION_ID;

import java.util.HashMap;

import org.mozzes.application.common.session.SessionIdProvider;
import org.mozzes.invocation.common.Invocation;
import org.mozzes.invocation.common.handler.InvocationHandler;
import org.mozzes.remoting.common.RemotingAction;
import org.mozzes.remoting.common.RemotingActionExecutorProvider;
import org.mozzes.remoting.common.RemotingException;
import org.mozzes.remoting.common.RemotingResponse;


/**
 * Client side for remote invocation
 * 
 * Accepts the service call and sends request to the server by using the MozzesRemoting mechanism.Response from the
 * MozzesServer is returned as a result of the execution.
 * 
 * @see InvocationHandler
 * 
 * @author Kokan
 * @author Perica
 */
class RemoteInvocationHandler<I> implements InvocationHandler<I> {

	/** The session id provider. */
	private final SessionIdProvider sessionIdProvider;

	/** The client provider. */
	private final RemotingActionExecutorProvider clientProvider;

	RemoteInvocationHandler(RemotingActionExecutorProvider clientProvider, SessionIdProvider sessionIdProvider) {
		this.sessionIdProvider = sessionIdProvider;
		this.clientProvider = clientProvider;
	}

	/**
	 * @see InvocationHandler#invoke(Invocation)
	 */
	public Object invoke(Invocation<? super I> methodInvocation) throws Throwable {
		try {
			// set action's params that will be sent to the server 
			HashMap<Object, Object> params = new HashMap<Object, Object>();
			params.put(KEY_METHOD_INVOCATION, methodInvocation);
			params.put(KEY_SESSION_ID, sessionIdProvider.getSessionId());

			// executing the action and getting response
			RemotingResponse response = clientProvider.get().execute(new RemotingAction(KEY_ACTION_NAME, params));

			// get the result from the response
			return response.getParam(KEY_RESULT);

		} catch (RemotingException ex) {
			throw ex.getCause() != null ? ex.getCause() : ex;
		}
	}

}
