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
