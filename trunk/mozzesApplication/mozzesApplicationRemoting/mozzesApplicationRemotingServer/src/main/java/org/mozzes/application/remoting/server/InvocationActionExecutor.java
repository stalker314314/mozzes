package org.mozzes.application.remoting.server;

import static org.mozzes.application.remoting.common.RemoteInvocationConstants.KEY_METHOD_INVOCATION;
import static org.mozzes.application.remoting.common.RemoteInvocationConstants.KEY_RESULT;
import static org.mozzes.application.remoting.common.RemoteInvocationConstants.KEY_SESSION_ID;

import java.util.HashMap;

import org.mozzes.application.plugin.request.RequestProcessor;
import org.mozzes.invocation.common.Invocation;
import org.mozzes.remoting.common.RemotingAction;
import org.mozzes.remoting.common.RemotingActionExecutor;
import org.mozzes.remoting.common.RemotingException;
import org.mozzes.remoting.common.RemotingResponse;

import com.google.inject.Inject;

/**
 * This is the class that represents {@link RemotingAction} and also a MozzesServer's service that remote client is
 * called.<br>
 * Basically this remote action is wrapping the invocation of the service and it's executing the service call on the
 * server by delegating the invocation to the {@link RequestProcessor}
 * 
 * @see RemotingActionExecutor
 */
class InvocationActionExecutor implements RemotingActionExecutor {

	private final RequestProcessor requestProcessor;

	@Inject
	InvocationActionExecutor(RequestProcessor requestProcessor) {
		this.requestProcessor = requestProcessor;
	}

	/**
	 * Executes the remoteAction(service invocation)
	 * 
	 * @see RemotingActionExecutor#execute(RemotingAction)
	 */
	public RemotingResponse execute(RemotingAction action) throws RemotingException {
		/* extract invocation from remoteAction */
		Invocation<?> invocation = (Invocation<?>) action.getParam(KEY_METHOD_INVOCATION);
		String sessionId = (String) action.getParam(KEY_SESSION_ID);

		if (invocation == null)
			throw new RemotingException("Invalid method invocation");

		try {
			/* process the invocation */
			Object invocationResult = requestProcessor.process(sessionId, invocation);

			/* put in the responseParams invicationResult and return RemotingResponse */
			HashMap<String, Object> responseParams = new HashMap<String, Object>();
			responseParams.put(KEY_RESULT, invocationResult);

			return new RemotingResponse(responseParams);
		} catch (Throwable e) {
			throw new RemotingException(e);
		}
	}
}
