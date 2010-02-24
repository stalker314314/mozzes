package org.mozzes.application.demo.mockups.services.internal.impl;

import org.mozzes.application.demo.mockups.services.internal.*;

import com.google.inject.*;

/**
 * This is implementation for the {@link PublicServiceCallingInternal} service
 */
public class PublicServiceCallingInternalImpl implements PublicServiceCallingInternal {

	@Inject
	SimpleInternalService internalService;

	/**
	 * @see PublicServiceCallingInternal#getFromInternal()
	 */
	@Override
	public int getFromInternal() {
		return internalService.getInteger();
	}
}
