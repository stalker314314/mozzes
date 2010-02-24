package org.mozzes.application.server.request.impl;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;
import org.mozzes.application.server.lifecycle.MozzesServerLifeCycleStatus;
import org.mozzes.application.server.mockups.MockUpInjector;
import org.mozzes.application.server.mockups.MockUpInvocation;
import org.mozzes.application.server.mockups.MockUpSessionManager;
import org.mozzes.application.server.mockups.ServerService1;
import org.mozzes.application.server.mockups.ServerService1Impl;
import org.mozzes.application.server.request.impl.RequestManagerImpl;
import org.mozzes.application.server.request.impl.RequestProcessorImpl;
import org.mozzes.application.server.session.impl.SessionContext;
import org.mozzes.invocation.guice.InternalInjectorProvider;


public class TestRequestProcessor {

	@Test
	public void test() {
		try {

			MockUpSessionManager sessionManager = new MockUpSessionManager();
			SessionContext sessionContext = sessionManager.requestStarted(null);

			RequestManagerImpl requestManager = new RequestManagerImpl();
			requestManager.start(sessionContext);

			InternalInjectorProvider injectorProvider = new InternalInjectorProvider();
			HashMap<Class<?>, Object> map = new HashMap<Class<?>, Object>();
			map.put(ServerService1.class, new ServerService1Impl());

			injectorProvider.setInjector(new MockUpInjector(map));

			MozzesServerLifeCycleStatus status = new MozzesServerLifeCycleStatus();
			status.setStarted();
			RequestProcessorImpl procesor = new RequestProcessorImpl(sessionManager, requestManager, injectorProvider
					.get(), status);

			MockUpInvocation<ServerService1> invocation = new MockUpInvocation<ServerService1>(ServerService1.class,
					"service1Method1", null, null);

			procesor.process(null, invocation);
			Assert.assertTrue(invocation.isInvoked());
		} catch (Throwable e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testStartRequstWithoutSession() {
		try {

			MockUpSessionManager sessionManager = new MockUpSessionManager();

			RequestManagerImpl requestManager = new RequestManagerImpl();

			InternalInjectorProvider injectorProvider = new InternalInjectorProvider();

			HashMap<Class<?>, Object> map = new HashMap<Class<?>, Object>();
			map.put(ServerService1.class, new ServerService1Impl());

			injectorProvider.setInjector(new MockUpInjector(map));

			MozzesServerLifeCycleStatus status = new MozzesServerLifeCycleStatus();
			status.setStarted();
			RequestProcessorImpl procesor = new RequestProcessorImpl(sessionManager, requestManager, injectorProvider
					.get(), status);

			MockUpInvocation<ServerService1> invocation = new MockUpInvocation<ServerService1>(ServerService1.class,
					"service1Method1", null, null);

			procesor.process(null, invocation);

			Assert.assertTrue(invocation.isInvoked());

		} catch (Throwable e) {
			Assert.fail(e.getMessage());
		}
	}
}
