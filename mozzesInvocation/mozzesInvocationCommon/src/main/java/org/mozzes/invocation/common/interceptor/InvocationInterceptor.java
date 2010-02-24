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
