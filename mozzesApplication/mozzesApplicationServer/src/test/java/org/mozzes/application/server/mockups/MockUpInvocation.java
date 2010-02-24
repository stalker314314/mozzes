package org.mozzes.application.server.mockups;

import org.mozzes.invocation.common.Invocation;

public class MockUpInvocation<I> extends Invocation<I> {

	private static final long serialVersionUID = 1L;

	private boolean invoked = false;

	public MockUpInvocation(Class<I> interfaceClass, String methodName, Class<?>[] methodParameterTypes,
			Object[] methodInvocationArguments) {
		super(interfaceClass, methodName, methodParameterTypes, methodInvocationArguments);

	}

	@Override
	public <II extends I> Object invoke(II target) throws Throwable {
		invoked = true;
		return new Object();
	}

	public boolean isInvoked() {
		return invoked;
	}	
}
