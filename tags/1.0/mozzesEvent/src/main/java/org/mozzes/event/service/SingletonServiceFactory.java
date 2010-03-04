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
 * Implementacija ServiceFactory-ja koja uvek vraca istu instancu servisa, onu koju je dobila prilikom kreiranja.
 * Koristi se ukoliko nije potrebno kreirati novu instancu fabrike za kreiranje servisa svaki put kada se trazi njeno
 * kreiranje.
 * 
 * @author Perica Milosevic
 * @version 1.8.2
 * @see ServiceFactory
 */
public class SingletonServiceFactory implements ServiceFactory {

	private Object serviceImplementation;

	public SingletonServiceFactory(Object serviceImplementation) {
		this.serviceImplementation = serviceImplementation;
	}

	public Object create() {
		return serviceImplementation;
	}

}
