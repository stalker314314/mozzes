package org.mozzes.application.demo.mockups.services.internal;

/**
 * This is simple service that's using server's internal service {@link SimpleInternalService}
 * 
 * @author vita
 */
public interface PublicServiceCallingInternal {

	/**
	 * @return the value from {@link SimpleInternalService#getInteger()}
	 */
	int getFromInternal();
}
