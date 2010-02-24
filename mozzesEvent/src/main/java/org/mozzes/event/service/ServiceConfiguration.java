package org.mozzes.event.service;

/**
 * Konfiguracija servisa. Sve vezano za konfiguraciju svih servisa treba da stoji ovde. Trenutno postoji samo dohvatanje
 * fabrike koja proizvodi servis.
 * 
 * @author Perica Milosevic
 * @version 1.8.2
 */
public interface ServiceConfiguration {

	/**
	 * Vraca fabriku za proizvodnju servisa
	 * 
	 * @return Fabrika za proizvodnju servisa
	 * @throws ServiceException Ukoliko nije moguce dohvatiti fabriku za proizvodnju servisa
	 */
	public ServiceFactory getServiceFactory() throws ServiceException;

}