package org.mozzes.application.demo.mockups.services.scoped.request.impl;

import org.mozzes.application.demo.mockups.services.scoped.request.*;
import org.mozzes.application.module.scope.*;

import com.google.inject.*;

/**
 * This service is annotated with the {@link RequestScoped} and so in the request there's one single instance of this
 * class and all attributes of this class are preserved during the request.
 * 
 * @author vita
 * 
 * @see RequestScopedService
 */
@RequestScoped
public class RequestScopedServiceImpl implements RequestScopedService {

	private int counter = 0;

	@Inject
	private RequestScopedService2 injectedTargetService;

	@Inject
	private RequestScopedService3 injectedServiceCallingTargetService;

	/**
	 * @see RequestScopedService#increment()
	 */
	@Override
	public void increment() {
		counter++;
	}

	/**
	 * @see RequestScopedService#getCounter()
	 */
	@Override
	public int getCounter() {
		return counter;
	}

	/**
	 * @see RequestScopedService#incrementInjectedAndReturnValue()
	 */
	@Override
	public int incrementInjectedAndReturnValue() {
		injectedTargetService.increment();
		injectedServiceCallingTargetService.incrementInInjectedService();
		return injectedTargetService.getCounter();
	}
}