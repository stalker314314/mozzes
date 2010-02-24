package org.mozzes.application.demo.mockups.services.scopedata.impl;

import org.mozzes.application.demo.mockups.scopedata.*;
import org.mozzes.application.demo.mockups.services.scopedata.*;
import org.mozzes.application.module.scope.*;

import com.google.inject.*;

/**
 * Implements the {@link ServiceWithRequestData} interface.<br>
 * 
 * It has data stored in the request context and injected another service that's also using the request context
 * 
 * @author vita
 */
public class ServiceWithRequestDataImpl implements ServiceWithRequestData {

	/**
	 * This is how the request data is obtained. (Data stored in the session is annotated with the {@link RequestScoped}
	 * annotation.
	 */
	@Inject
	private MRequestData requestData;

	/**
	 * This is the injecting of the some other service. Type should be the interface and not the service implementation
	 * class.<br>
	 * 
	 * <code>@Inject <br>private MServerService2Impl service2; </code> this is not good
	 */
	@Inject
	private ServiceWithRequestData2 otherServiceWithRequestContext;

	/**
	 * increment value of the object stored in the request... if the object is really stored in the request then calling
	 * the some other service that also works with that value would preserve value because both service invocations are
	 * in the same request.(there's only one "root" service method invocation in the request but that method can call
	 * some other services. (in this example actionWithRequestContext() is the root service invocation and
	 * incrementRequestCounter is the next invocation in the same request)
	 * 
	 * @see ServiceWithRequestData#incrementAndReturnInTheRequestContext()
	 */
	@Override
	public int incrementAndReturnInTheRequestContext() {
		requestData.increment();
		/* this is in the same request so the MTransactionData instance is the same */
		otherServiceWithRequestContext.incrementRequestCounter();
		return requestData.getCounter();
	}
}
