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
package org.mozzes.remoting.common;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import org.mozzes.remoting.common.netty.Unique;


/**
 * Encapsulates concrete action remoting client sends to remoting server
 * 
 * @author Perica Milosevic
 */
public final class RemotingAction implements Unique, Serializable {

	private static final long serialVersionUID = -806957954023327547L;

	/** Name of the action that clients wants to execute */
	private final String actionName;
	
	private Long id;

	/** Action parameters represented as Map */
	private Map<? extends Object, ? extends Object> actionParams = null;

	/**
	 * Default constructor for action
	 * 
	 * @param actionName Name of this action
	 * @param actionParams Parameters of this action
	 */
	public RemotingAction(String actionName, Map<? extends Object, ? extends Object> actionParams) {
		if (actionName == null)
			throw new IllegalArgumentException("Invalid action name - null");

		this.actionName = actionName;
		this.actionParams = actionParams;
	}

	/**
	 * @return Name of this action
	 */
	public String getActionName() {
		return actionName;
	}

	/**
	 * @return Parameters of this action
	 */
	public Map<? extends Object, ? extends Object> getParams() {
		return Collections.unmodifiableMap(actionParams);
	}

	/**
	 * @param key Key to retrieve object from parameters
	 * @return Specified parameter based on parameter key
	 */
	public Object getParam(Object key) {
		if (actionParams == null)
			return null;
		return actionParams.get(key);
	}

	@Override
	public String toString() {
		return getActionName();
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getId() {
		return id;
	}

}
