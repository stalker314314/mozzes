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
package org.mozzes.invocation.common.interceptor;

import org.mozzes.invocation.common.Invocation;
import org.mozzes.invocation.common.handler.InvocationHandler;

/**
 * {@link InvocationInterceptor} is interface implemented by objects capable of intercepting method invocation calls
 * before or after the real processing is performed by {@link InvocationHandler}
 * 
 * @author Perica Milosevic
 */
public interface InvocationInterceptor {

	/**
	 * Intercepts method invocation and performs some processing before or after the method invocation is handled by
	 * {@link InvocationHandler}.
	 * 
	 * Good example of {@link InvocationInterceptor} usage is profiling interceptor used to measures time passed during
	 * method invocation. <code>
	 * <pre>
	 * public <I> Object invoke(Invocation<? super I> invocation, InvocationHandler<I> target) throws Throwable {
	 *   long startTime = System.currentTimeMillis();
	 *     try {
	 *       return target.invoke(invocation);
	 *     } finally {
	 *       System.out.println("Method execution time for " + invocation.getMethodName() + ": "
	 *           + (System.currentTimeMillis() - startTime) + "ms");
	 *   }
	 * }
	 * </pre>
	 * </code>
	 * 
	 * @param <I> Interface intercepted by this interceptor
	 * @param invocation Method invocation
	 * @param target Invocation handler which will handle method invocation
	 * @return Invocation result
	 * @throws Throwable any exception thrown during invocation handling or intercept
	 */
	public <I> Object invoke(Invocation<? super I> invocation, InvocationHandler<I> target) throws Throwable;

}
