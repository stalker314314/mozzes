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
package org.mozzes.event.manager;

/**
 * Servis koji omogucava prijavljivanje na pracenje raznih dogadjaja u sistemu.
 * 
 * @author Perica Milosevic
 * @version 1.8.2
 */
public interface EventManagerService {

	static final String SERVICE_TYPE = "eventManager://";

	/**
	 * EventHandler se prijavljuje kako bi primao obavestenja o svim eventima koji su se desili.
	 * 
	 * @param useEventQueue Ukoliko je vrednost ovog parametra true onda inicijator dogadjaja ne mora da ceka dok
	 *            handler radi obradu dogadjaja. Ukoliko je vrednost ovog parametra false onda inicijator dogadjaja ceka
	 *            dok handler zavrsi obardu i u slucaju bilo kakvog exceptiona inicijator ce biti obavesten.
	 * @param eventHandler EventHandler
	 * @throws EventManagerException ukoliko nije uspela prijava
	 */
	public void subscribe(Object eventHandler, boolean useEventQueue) throws EventManagerException;

	/**
	 * Odjavljivanje EventHandler-a.
	 */
	public void unsubscribe(Object eventHandler);

	/**
	 * Vraca broj prijavljenih subscribera
	 * 
	 * @return Broj subscribera
	 */
	public int getNumberOfSubscribers();

}
