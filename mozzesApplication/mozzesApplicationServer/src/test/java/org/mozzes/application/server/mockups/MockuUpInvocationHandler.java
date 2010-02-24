package org.mozzes.application.server.mockups;

import org.mozzes.invocation.common.Invocation;
import org.mozzes.invocation.common.handler.InvocationHandler;

public class MockuUpInvocationHandler implements InvocationHandler<ServerService1> {

	private boolean fail = false;

	private boolean isIgnored = false;

	public MockuUpInvocationHandler(boolean fail) {
		this.fail = fail;
	}

	public MockuUpInvocationHandler(boolean fail, boolean isIgnored) {
		this.fail = fail;
		this.isIgnored = isIgnored;
	}

	public MockuUpInvocationHandler() {
		// empty
	}

	@Override
	public Object invoke(Invocation<? super ServerService1> invocation) throws Throwable {
		
		Object returnValue = invocation.invoke(null);
		
		if (fail) {
			if (isIgnored)
				throw new MockUpIgnoredException();

			throw new RuntimeException();
		}
		return returnValue;
	}

}