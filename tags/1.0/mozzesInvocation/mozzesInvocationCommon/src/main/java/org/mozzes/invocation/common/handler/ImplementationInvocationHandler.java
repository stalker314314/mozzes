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
package org.mozzes.invocation.common.handler;

import org.mozzes.invocation.common.Invocation;

/**
 * {@link InvocationHandler} implementation which holds object that implements interface I and delegates all method
 * invocations to that object.
 * 
 * @author Perica Milosevic
 */
public class ImplementationInvocationHandler<I> implements InvocationHandler<I> {

	/**
	 * Real interface implementation
	 */
	private final I implementation;

	/**
	 * @param <II> Class which implements interface I
	 * @param implementation Class II instance
	 */
	public <II extends I> ImplementationInvocationHandler(II implementation) {
		this.implementation = implementation;
	}

	@Override
	public Object invoke(Invocation<? super I> invocation) throws Throwable {
		return invocation.invoke(implementation);
	}

}
