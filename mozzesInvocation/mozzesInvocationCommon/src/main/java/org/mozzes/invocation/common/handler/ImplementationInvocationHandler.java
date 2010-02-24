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
