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

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Red cekanja na kome se nalazi svi dogadjaji koji cekaju obradu. Implementiran je kao BlockingQueue.
 * 
 * @author Perica Milosevic
 * @version 1.7.5
 */
class EventQueue {

	/**
	 * Svi eventi koji cekaju obradu nalaze se u ovoj listi.
	 */
	private BlockingQueue<EventDispatchData> eventQueue;

	EventQueue() {
		eventQueue = new LinkedBlockingQueue<EventDispatchData>();
	}

	/**
	 * Stavljanje dogadjaja na red cekanja
	 * 
	 * @param event Dogadjaj
	 * @param handlers Hendleri
	 */
	void enqueueEvent(EventDetails event, List<Object> handlers) {
		for (Iterator<Object> handlersIterator = handlers.iterator(); handlersIterator.hasNext();) {
			this.eventQueue.add(new EventDispatchData(event, handlersIterator.next()));
		}
	}

	/**
	 * Skida prvi raspolozivi dogadjaj sa reda cekanja. Ukoliko nema slobodnih dogadjaj metoda ce blokirati sve dok se
	 * ne pojavi nesto.
	 * 
	 * @return Dogadjaj sa reda cekanja.
	 * @throws InterruptedException Ukoliko je doslo do neke greske u toku cekanja da se pojavi neki dogadjaj na redu
	 *             cekanja.
	 */
	EventDispatchData getEvent() throws InterruptedException {
		return this.eventQueue.take();
	}

}
