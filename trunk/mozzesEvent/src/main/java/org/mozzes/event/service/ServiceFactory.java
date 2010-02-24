package org.mozzes.event.service;

/**
 * Fabrika za proizvodnju servisa. Duznost joj je da kreira servis na zahtev. Deo je konfiguracije servisa i koristi je
 * {@link ServiceFactory} da bi dohvatio implementaciju servisa.
 * 
 * @author Perica Milosevic
 * @version 1.8.2
 * @see ServiceConfiguration
 */
public interface ServiceFactory {

	/**
	 * Proizvodi instancu koja implementira trazeni servis
	 * 
	 * @return Instanca implementacije trazenog servisa
	 * @throws ServiceException Ukoliko iz bilo kog razloga nije uspelo kreiranje servisa
	 */
	public Object create() throws ServiceException;

}
