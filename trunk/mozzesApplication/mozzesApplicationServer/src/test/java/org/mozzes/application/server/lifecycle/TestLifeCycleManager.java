package org.mozzes.application.server.lifecycle;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.mozzes.application.server.MozzesTestBase;
import org.mozzes.application.server.lifecycle.MozzesServerLifeCycleManager;


public class TestLifeCycleManager extends MozzesTestBase {

	@Test
	public void testServiceMethodCall() {
		MozzesServerLifeCycleManager manager = new MozzesServerLifeCycleManager();

		try {
			manager.addServerListeners(null);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}
