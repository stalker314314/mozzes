package org.mozzes.application.remoting.common;

/**
 * The Class RemoteInvocationConstants holds constraints that are necessary for using the mozzesRemote framework(for
 * remote clients to connect).
 */
public interface RemoteInvocationConstants {

	/**
	 * name of the action for executing remote method invocation of server services.
	 */
	final String KEY_ACTION_NAME = "remoteMethodInvocation";

	/** Key in the RemotingAction associated with the method invocation informations */
	final String KEY_METHOD_INVOCATION = "methodInvocation";

	/** Key in the remote action associated with session id of the logged user. */
	final String KEY_SESSION_ID = "KEY_SESSION_ID";

	/** Key in the RemotingResponse associated with result of method invocation */
	final String KEY_RESULT = "result";
}
