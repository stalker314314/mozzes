package org.mozzes.application.demo.mockups.services.basic.impl;

import org.mozzes.application.demo.mockups.services.basic.*;

import com.google.inject.*;

/**
 * This is the bad implementation of the interface because it's injecting service implementation class rather than
 * service interface
 * 
 * @author vita
 */
public class ServiceThatInjectOtherServiceImplBad implements ServiceThatInjectOtherService {

	/**
	 * THIS IS NOT OK.this shouldn't be injected
	 */
	@Inject
	BasicServiceImpl service1;

	/**
	 * @see ServiceThatInjectOtherService#getIntegerFromInjectedService()
	 */
	@Override
	public int getIntegerFromInjectedService() {
		return service1.getIntegerFromServer().intValue();
	}
}