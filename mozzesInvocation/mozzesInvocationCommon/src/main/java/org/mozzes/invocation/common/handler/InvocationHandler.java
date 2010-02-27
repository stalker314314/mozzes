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

import java.lang.reflect.UndeclaredThrowableException;

import org.mozzes.invocation.common.Invocation;
import org.mozzes.invocation.common.InvocationProxy;


/**
 * {@link InvocationHandler} is the interface implemented by the <i>invocation handler</i> of a proxy instance.
 * 
 * <p>
 * Each {@link InvocationProxy} instance has an associated invocation handler. When a method is invoked on a proxy
 * instance, the method invocation is encoded and dispatched to the <code>invoke</code> method of its invocation
 * handler.
 * 
 * @see Invocation 
 * @see InvocationProxy
 * 
 * @author Perica Milosevic
 * @author Kokan
 */
public interface InvocationHandler<I> {

	/**
	 * Processes a method invocation on a proxy instance and returns the result. This method will be invoked on an
	 * invocation handler when a method is invoked on a proxy instance that it is associated with.
	 * 
	 * @param invocation Method invocation
	 * @return Method invocation result
	 * @throws Throwable the exception to throw from the method invocation on the proxy instance. The exception's type
	 *            must be assignable either to any of the exception types declared in the <code>throws</code> clause of
	 *            the interface method or to the unchecked exception types <code>java.lang.RuntimeException</code> or
	 *            <code>java.lang.Error</code>. If a checked exception is thrown by this method that is not assignable to
	 *            any of the exception types declared in the <code>throws</code> clause of the interface method, then an
	 *            {@link UndeclaredThrowableException} containing the exception that was thrown by this method will be
	 *            thrown by the method invocation on the proxy instance.
	 */
	Object invoke(Invocation<? super I> invocation) throws Throwable;

}
