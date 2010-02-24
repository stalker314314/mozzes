package org.mozzes.invocation.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;

import org.mozzes.invocation.common.handler.InvocationHandler;


/**
 * This class that represents fake implementation of an interface I. <br>
 * <br>
 * Each {@link InvocationProxy} instance has an associated <i>invocation handler</i> object, which implements the
 * interface {@link InvocationHandler}. A method invocation on a proxy instance through interface I will be delegated to
 * the {@link InvocationHandler#invoke(Invocation)} method of the instance's invocation handler.<br>
 * The invocation handler processes the encoded method invocation as appropriate and the result that it returns will be
 * returned as the result of the method invocation on the proxy instance.
 * 
 * @see Invocation
 * @see InvocationHandler
 * 
 * @author Kokan
 * @author Perica Milosevic
 */
public class InvocationProxy<I> implements java.lang.reflect.InvocationHandler {

	/** Class which represents interface I */
	private Class<I> invocationInterface;

	/** Handler that accepts method calls */
	private InvocationHandler<I> invocationHandler;

	/**
	 * {@link InvocationProxy} creation
	 * @param <I> interface <i>"implemented"</i> by this proxy
	 * @param invocationInterface Class which represents interface I
	 * @param handler which performs method invocation processing 
	 * @return {@link InvocationProxy} instance
	 */
	@SuppressWarnings("unchecked")
	public static <I> I newInstance(Class<I> invocationInterface, InvocationHandler<I> handler) {
		return (I) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
				new Class<?>[] { invocationInterface }, new InvocationProxy<I>(invocationInterface, handler));
	}

	private InvocationProxy(Class<I> invocationInterface, InvocationHandler<I> handler) {
		setInvocationInterface(invocationInterface);
		setHandler(handler);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		try {
			Invocation<I> methodInvocation = new Invocation<I>(invocationInterface, method.getName(), 
			        method.getParameterTypes(), args);

			return invocationHandler.invoke(methodInvocation);
			
		} catch (Throwable thr) {
			
		    if (InvocationTargetException.class.isInstance(thr))
				thr = ((InvocationTargetException) thr).getTargetException();

			if (RuntimeException.class.isInstance(thr))
				throw thr;

			if (Error.class.isInstance(thr))
				throw thr;

			Class<?>[] declaredExceptions = method.getExceptionTypes();
			
			for (int i = 0; i < declaredExceptions.length; i++) {
				if (declaredExceptions[i].isInstance(thr))
					throw thr;
			}
			throw new UndeclaredThrowableException(thr);
		}
	}

	private void setInvocationInterface(Class<I> invocationInterface) {
		if (invocationInterface == null)
			throw new IllegalArgumentException("Invalid invocation interface - null");

		if (!invocationInterface.isInterface())
			throw new IllegalArgumentException("Invalid invocation interface - class instead of interface");

		this.invocationInterface = invocationInterface;
	}

	private void setHandler(InvocationHandler<I> handler) {
		if (handler == null)
			throw new IllegalArgumentException("Invalid invocation handler - null");

		this.invocationHandler = handler;
	}

}
