package org.mozzes.application.demo.mockups.services.scopedata.impl;

import org.mozzes.application.demo.mockups.scopedata.*;
import org.mozzes.application.demo.mockups.services.scopedata.*;
import org.mozzes.application.module.scope.*;

import com.google.inject.*;

/**
 * This is the implementation of the {@link ServiceWithSessionData} service that preserves the value of the simple
 * counter between invocations in the same session
 * 
 * @author vita
 */
public class ServiceWithSessionDataImpl implements ServiceWithSessionData {

	/**
	 * This is how the session data is obtained. (Data stored in the session is annotated with the {@link SessionScoped}
	 * annotation.
	 */
	@Inject
	private MSessionData sessionData;

	/**
	 * Just increment the value in the object that is marked with the SessionScoped annotation. When we call this method
	 * first time counter will have value 0 and every next time it will increment.(in the same session)
	 * 
	 * @see ServiceWithSessionData#incrementSessionCounter()
	 */
	@Override
	public void incrementSessionCounter() {
		sessionData.increment();
	}

	/**
	 * Here we just return the value in the counter. All calls to the incrementSessionCounter in the same session
	 * incremented this value.
	 * 
	 * @see ServiceWithSessionData#getSessionCounterValue()
	 */
	@Override
	public int getSessionCounterValue() {
		return sessionData.getCounter();
	}
}
