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
