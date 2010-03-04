/*
 * Copyright 2010 Mozzart
 *
 *
 * This file is part of mozzes.
 *
 * mozzes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mozzes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mozzes.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
