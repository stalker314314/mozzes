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
