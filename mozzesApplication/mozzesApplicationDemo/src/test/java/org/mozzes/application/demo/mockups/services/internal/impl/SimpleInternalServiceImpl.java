package org.mozzes.application.demo.mockups.services.internal.impl;

import org.mozzes.application.demo.mockups.services.internal.*;

/**
 * This is the implementation for the {@link SimpleInternalService} service.
 * 
 * @author vita
 */
public class SimpleInternalServiceImpl implements SimpleInternalService {

	/**
	 * @see SimpleInternalService#getInteger()
	 */
	public int getInteger() {
		return SimpleInternalService.result;
	}
}
