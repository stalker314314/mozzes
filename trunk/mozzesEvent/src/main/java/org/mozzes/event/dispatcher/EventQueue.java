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
