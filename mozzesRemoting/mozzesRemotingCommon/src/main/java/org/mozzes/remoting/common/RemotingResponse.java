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
 * Holds all information remoting server returned back to remoting client as response to executed action
 * 
 * @author Perica Milosevic
 */
public class RemotingResponse implements Serializable, Unique {

	private static final long serialVersionUID = 4124876546041436541L;
	
	private Long id;

	/** Response parameters */
	private Map<? extends Object, ? extends Object> responseParams = null;

	/**
	 * Empty remoting response
	 */
	public RemotingResponse() {
		this(null);
	}

	/**
	 * Constructor that creates new response with a given parameters
	 * @param params Response parameters
	 */
	public RemotingResponse(Map<? extends Object, ? extends Object> params) {
		this.responseParams = params;
	}

	/**
	 * @return Response parameters
	 */
	public Map<? extends Object, ? extends Object> getParams() {
		return Collections.unmodifiableMap(responseParams);
	}

	/**
	 * @param key Key to retreive parameter
	 * @return Specified parameter based on parameter key
	 */
	public Object getParam(Object key) {
		if (responseParams == null)
			return null;
		return responseParams.get(key);
	}
	
	/**
	 * Return id of this remoting response. This remoting response id has the same ID as the RemotingAction
	 * which response is this object.
	 * @return a unique id which bind this response to remoting action.
	 */
	@Override
	public Long getId() {
		return id;
	}
	
	/**
	 * Set id of this remoting response. This remoting response id has the same ID as the RemotingAction which response
	 * is this object.
	 * @param id a unique id of the response. Must correspond to id of an remoting action.
	 */
	@Override
	public void setId(Long id) {
		this.id = id;
	}
	
}
