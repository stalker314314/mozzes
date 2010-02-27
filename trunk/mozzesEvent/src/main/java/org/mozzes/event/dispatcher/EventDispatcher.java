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
package org.mozzes.event.dispatcher;

import org.mozzes.event.Event;

/**
 * EventDispatcher-i prosledjuju dogadjaj prijavljenom handleru na obradu.
 * 
 * @author Perica Milosevic
 * @version 1.8.2
 * @see Event
 */
public interface EventDispatcher {

	/**
	 * Prosledjivanje dogadjaja prijavljenom handleru na obradu
	 * 
	 * @param eventDispatchData Informacije o dogadjaju i prijavljenom handleru
	 * @throws Throwable Greska nastala prilikom obrade dogadjaja
	 */
	public void dispatch(EventDispatchData eventDispatchData) throws Throwable;
}
