package org.mozzes.event.dispatcher;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Klasa zaduzena za dispatch dogadjaja u sistemu.
 * 
 * Prvo se pozivaju svi handleri koji su zahtevali da inicijator dogadjaja ceka zavrsetka obrade. Ukoliko dodje do neke
 * greske prilikom obrade cela transakcija pada, cak i transakcija u kojoj se dogadjaj desio.
 * 
 * Ukoliko prva obrada ispravno prodje dogadjaj ide na queueu kako bi kasnije bio obradjen od handlera koji su se
 * prilikom prijave izjasnili da inicijator dogadjaja ne mora da ceka kraj obrade. Ukoliko dodje do neke greske prilikom
 * obrade dogadjaja od strane tih handera inicijalna transakcija nece biti narusena.
 * 
 * @author Perica Milosevic
 * @version 1.7.5
 */
public class EventDispatchManager {
	private static final Logger logger = LoggerFactory.getLogger(EventDispatchManager.class);

	/**
	 * Red cekanja na koji idu svi dogadjaji.
	 */
	private EventQueue eventQueue;

	/**
	 * Spisak Threadova koji obradjuju dogadjaje sa reda cekanja (za svaki EventManager postoji po jedan thread za
	 * obradu dogadjaja).
	 */
	private LinkedList<EventDispatchThread> dispatchers;

	public EventDispatchManager(EventDispatcher eventDispatcher, int numberOfDispatchThreads) {
		eventQueue = new EventQueue();
		dispatchers = new LinkedList<EventDispatchThread>();
		initDispatchThreads(eventDispatcher, numberOfDispatchThreads);
		EventPerformanceLogger.get();
	}

	/**
	 * Dispatch dogadjaja
	 * 
	 * @param dispatchMethod metoda koja se poziva kako bi se obavio dispatch
	 * @param dispatchMethodParams parametri koji se prosledjuju prilikom obrade
	 * @param handlersWithWait handleri koji su zahtevali obradu dogadjaja u okviru iste transakcije u kojoj se dogadjaj
	 *            i desio
	 * @param handlersWithQueue handleri koji su zahtevali obradu dogadjaja nakon zavrsetka transakcije u kojoj se
	 *            dogadjaj desio
	 * @throws Throwable greska prilikom obrade dogadjaja
	 */
	public void dispatch(Method dispatchMethod, Object[] dispatchMethodParams, List<Object> handlersWithWait,
			List<Object> handlersWithQueue) throws Throwable {
		EventDetails event = new EventDetails(dispatchMethod, dispatchMethodParams);

		// Obrada dogadjaja od strane handlera koji ne koriste queue
		for (Iterator<Object> iter = handlersWithWait.iterator(); iter.hasNext();) {
			event.dispatch(iter.next());
		}

		// Stavljamo dogadjaj na queueu za kasniju obradu
		enqueueDispatchEventAfterCommit(event, handlersWithQueue);
	}

	private EventQueue getEventQueue() {
		return this.eventQueue;
	}

	/**
	 * Postavlja listener-a na tekucu transakciju. Ukoliko transakcija bude uspesno obavljena event ce biti poslat na
	 * queue za obradu.
	 * 
	 * @param event Informacije o event-u koji ceka obradu.
	 * @param handlers EventHandleri koji su se prijavi za obradu dogadjaja uz koriscenje EventQueue-a
	 */
	private void enqueueDispatchEventAfterCommit(final EventDetails event, final List<Object> handlers) {
		if (handlers == null || handlers.isEmpty())
			return;

		/* ovo premestiti u transaction listener na uspesno izvrsenu transakciju */
		getEventQueue().enqueueEvent(event, handlers);

		// ako nije uspelo postavljanje listenera na tekucu transakciju
		// onda ce evet biti odmah stavljen na queue
		// if (!transactionListenerRegistered)
		// getEventQueue().enqueueEvent(event, handlers);
	}

	private void initDispatchThreads(EventDispatcher eventDispatcher, int numberOfDispatchThreads) {
		for (int i = 0; i < numberOfDispatchThreads; i++)
			addDispatcher(eventDispatcher);

		if (dispatchers.size() > 0) {
			logger.info("Threads for event proccessing created (" + dispatchers.size() + ")");
		}
	}

	/**
	 * Dodaje jos jedan Thread za obradu dogadjaja
	 */
	private void addDispatcher(EventDispatcher eventDispatcher) {
		EventDispatchThread dispatchThread = new EventDispatchThread(dispatchers.size() + 1, eventQueue,
				eventDispatcher);
		dispatchers.add(dispatchThread);
		dispatchThread.start();
	}
}
