package org.mozzes.application.demo.mockups.services.scoped.request.impl;

import org.mozzes.application.demo.mockups.services.scoped.request.*;
import org.mozzes.application.module.scope.*;

import com.google.inject.*;

/**
 * This service just delegates call to the {@link RequestScopedService2#increment()} service method.<br>
 * 
 * Because {@link RequestScopedService2Impl} is annotated with {@link RequestScoped} this should be the same instance as
 * in the {@link RequestScopedServiceImpl} service.
 * 
 * @author vita
 * 
 * @see RequestScopedService
 * @see RequestScopedServiceImpl
 * 
 * @see RequestScopedService2
 * @see RequestScopedService2Impl
 */
public class RequestScopedService3Impl implements RequestScopedService3 {

	@Inject
	private RequestScopedService2 injectedService;

	@Override
	public void incrementInInjectedService() {
		injectedService.increment();
	}
}