package org.mozzes.application.server;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mozzes.application.common.client.MozzesClient;
import org.mozzes.application.server.MozzesServer;
import org.mozzes.application.server.MozzesServerConfiguration;
import org.mozzes.application.server.mockups.ServerService1;
import org.mozzes.application.server.mockups.ServerService1Impl;


public class TestMozzesServer extends MozzesTestBase {

	private MozzesServer server;

	@Before
	public void beforeTest() {
		MozzesServerConfiguration serverConfig = new MozzesServerConfiguration();

		serverConfig.addService(ServerService1.class, ServerService1Impl.class);
		server = new MozzesServer(serverConfig);
		server.start();
	}

	@After
	public void afterTest() {
		server.stop();
	}

	@Test
	public void testServiceMethodCall() {
		assertNotNull(server);
		MozzesClient client = server.getLocalClient();
		assertNotNull(client);
		client.getService(ServerService1.class).service1Method1();
	}

}
