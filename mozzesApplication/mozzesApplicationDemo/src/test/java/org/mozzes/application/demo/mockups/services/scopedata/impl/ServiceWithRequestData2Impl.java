package org.mozzes.application.demo.mockups.services.scopedata.impl;

import org.mozzes.application.demo.mockups.scopedata.*;
import org.mozzes.application.demo.mockups.services.scopedata.*;

import com.google.inject.*;

/**
 * Implements the {@link ServiceWithRequestData2} service.<br>
 * 
 * It's just incrementing the value of the counter in the transaction context
 * 
 * @author vita
 */
public class ServiceWithRequestData2Impl implements ServiceWithRequestData2 {

	@Inject
	MRequestData requestData;

	/**
	 * @see ServiceWithRequestData2#incrementRequestCounter()
	 */
	@Override
	public void incrementRequestCounter() {
		requestData.increment();
	}
}
