package org.mozzes.application.server.mockups;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerService1Impl implements ServerService1 {

	private static final Logger logger = LoggerFactory.getLogger(ServerService1Impl.class);

	@Override
	public void service1Method1() {
		logger.info("service1Method1");
	}

	@Override
	public Integer service1Method2() {
		logger.info("service1Method2");
		return Integer.valueOf(123);
	}

	@Override
	public String service1Method3() {
		logger.info("service1Method3");
		return "return value service1Method3";
	}
}
