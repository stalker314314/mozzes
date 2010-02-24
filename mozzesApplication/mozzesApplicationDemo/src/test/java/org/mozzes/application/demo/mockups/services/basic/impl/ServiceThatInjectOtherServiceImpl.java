package org.mozzes.application.demo.mockups.services.basic.impl;

import org.mozzes.application.demo.mockups.services.basic.*;

import com.google.inject.*;

/**
 * This is the good implementation of the interface because it's injecting another service interface and not direct
 * implementation
 * 
 * @author vita
 */
public class ServiceThatInjectOtherServiceImpl implements ServiceThatInjectOtherService {

	/**
	 * this is OK. This should be injected
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