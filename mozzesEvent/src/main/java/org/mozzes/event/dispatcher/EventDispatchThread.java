package org.mozzes.event.dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread koji radi obradu dogadjaja koji se nalaze na redu cekanja za obradu.
 * 
 * @author Perica Milosevic
 * @version 1.7.5
 */
class EventDispatchThread extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(EventDispatchThread.class);

	/**
	 * Red cekanja sa koga skidamo dogadjaje koji cekaju obradu
	 */
	private EventQueue eventQueue;

	/**
	 * EventDispatcher koji prosledjuje dogadjaj handleru
	 */
	private EventDispatcher eventDispatcher;

	/**
	 * Kreiranje Threada za obradu dogadjaja
	 * 
	 * @param num Redni broj threada
	 * @param eventQueue Red cekanja sa koga ce skidati dogadjaje
	 * @param eventDispatcher EventDispatcher koji prosledjuje dogadjaj handleru
	 */
	EventDispatchThread(int num, EventQueue eventQueue, EventDispatcher eventDispatcher) {
		super("EventDispatchThread-" + num);
		setDaemon(true);
		this.eventQueue = eventQueue;
		this.eventDispatcher = eventDispatcher;
	}

	@Override
	public void run() {
		while (true) {
			try {
				dispatchEvent(getEventFromQueue());
			} catch (Throwable thr) {
				logger.error("Unknown error during event processing!", thr);
			}
		}
	}

	/**
	 * Skida jedan dogadjaj sa reda cekanja. Ukoliko trenutno nema dogadjaja ovaj thread ce biti na wait-u sve dok se
	 * nesto ne pojavi.
	 * 
	 * @return EventDispatchData dogadjaj sa queuea koji treba obraditi
	 */
	private EventDispatchData getEventFromQueue() {
		EventDispatchData event = null;
		while (event == null) {
			try {
				event = eventQueue.getEvent();
			} catch (InterruptedException e) {
				logger.error("Error during event processing! " + "Failure during getting event from queue !", e);
			}
		}
		return event;
	}

	/**
	 * Prosledjivanje dogadjaja na obradu od strane handlera
	 * 
	 * @param event Dogadjaj
	 * @throws Throwable greska nastala prilikom obrade dogadjaja
	 */
	private void dispatchEvent(EventDispatchData event) throws Throwable {
		eventDispatcher.dispatch(event);
	}

}