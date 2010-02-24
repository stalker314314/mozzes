package org.mozzes.event.service;

/**
 * Default implementacija ServiceConfiguration-a.
 * <p>
 * Prilikom kreiranja prihvata ServiceFactory i na zahtev ga vraca.
 * 
 * @author Perica Milosevic
 * @version 1.8.2
 * @see ServiceConfiguration
 */
public class DefaultServiceConfiguration implements ServiceConfiguration {
	/** instanca fabrike za kreiranje servisa */
	private ServiceFactory serviceFactory;

	/**
	 * Konstruktor koji prima fabriku za kreiranje servisa i cuva instancu
	 */
	public DefaultServiceConfiguration(ServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	public ServiceFactory getServiceFactory() {
		return serviceFactory;
	}

}
