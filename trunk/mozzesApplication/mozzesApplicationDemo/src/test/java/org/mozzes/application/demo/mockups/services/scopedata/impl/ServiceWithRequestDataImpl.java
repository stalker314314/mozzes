/*
 * Copyright 2010 Mozzart
 *
 *
 * This file is part of mozzes.
 *
 * mozzes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mozzes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mozzes.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
