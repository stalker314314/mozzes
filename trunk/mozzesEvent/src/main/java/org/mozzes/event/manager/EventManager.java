package org.mozzes.event.manager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;

import org.mozzes.event.dispatcher.EventDispatchManager;


/**
 * Ukoliko je potrebna funkcionalnost "Event -> EventHandler[]" onda se moze koristiti ovaj EventManager.
 * <p>
 * EventHandler-i se prijavljuju kod EventManager-a i bivaju obavesteni kad god se desi neki od dogadjaja koje oni znaju
 * da obrade.
 * <p>
 * EventManager je proxy koji presrece pozive metoda i na osnovu tih poziva generise dogadjaje. Te dogadjaje prosledjuje
 * svim handlerima koji su u tom trenutku pretpalaceni na taj dogadjaj.
 * <p>
 * Dispatch svih dogadjaja moze se obaviti u posebnom (EventDispatch) Thread-u i kreator eventa tada ne mora cekati
 * obradjivanje dogadjaja, a moguca je i varijanta sa cekanjem da se obavi obrada dogadjaja. Prilikom prijavaljivana
 * svaki od Handler-a bira da li hoce da kreator dogadjaja ceka zavrsetak obrade ili smatra da to nije potrebno.
 * 
 * @author Perica Milosevic
 * @version 1.7.4
 */
public class EventManager implements InvocationHandler, EventManagerService {

	/**
	 * List-a svih handler-a koji su se prijavili za obavestavanje kada se desi neki event.
	 * 
	 * Ovi handleri ne zahtevaju da inicijator dogadjaj ceka zavrsetak obrade.
	 */
	private LinkedList<Object> eventHandlersWithQueue;

	/**
	 * List-a svih handler-a koji su se prijavili za obavestavanje kada se desi neki event.
	 * 
	 * Ovi handleri zahtevaju da inicijator dogadjaj ceka zavrsetak obrade.
	 */
	private LinkedList<Object> eventHandlersWithWait;

	/**
	 * Ovaj interfejs moraju da implementiraju svi handleri koji se prijavljuju kod ovog menadzera.
	 */
	private Class<?> eventHandlerInterface;

	/**
	 * Dispatch manager koji prosledjuje event prijavljenim handlerima
	 */
	private EventDispatchManager eventDispatchManager;

	/**
	 * @param eventDispatchManager manager koji prosledjuje event prijavljenim handlerima
	 * @param eventHandlerInterface event interface
	 * @throws EventManagerException Ukoliko je prosledjen los event interface
	 */
	public EventManager(EventDispatchManager eventDispatchManager, Class<?> eventHandlerInterface)
			throws EventManagerException {
		setEventHandlerInterface(eventHandlerInterface);
		this.eventDispatchManager = eventDispatchManager;
		this.eventHandlersWithQueue = new LinkedList<Object>();
		this.eventHandlersWithWait = new LinkedList<Object>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.edg.common.event.EventManager#subscribe(java.lang.Object, boolean)
	 */
	public void subscribe(Object eventHandler, boolean useEventQueue) throws EventManagerException {
		if (eventHandler == null)
			throw new EventManagerException("Unable tu subscribe null event handler");

		if (!eventHandlerInterface.isInstance(eventHandler)) {
			throw new EventManagerException("Invalid event handler, expected handler: "
					+ eventHandlerInterface.getName());
		}

		/* izbaciti ovo kada pocnemo da koristimo queue */
		if (useEventQueue == true) {
			throw new EventManagerException("Event queue is not implemented currently");
		}
//
//		if (useEventQueue)
//			synchronized (eventHandlersWithQueue) {
//				eventHandlersWithQueue.add(eventHandler);
//			}
		else
			synchronized (eventHandlersWithWait) {
				eventHandlersWithWait.add(eventHandler);
			}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.edg.common.event.EventManager#unsubscribe(java.lang.Object)
	 */
	public void unsubscribe(Object eventHandler) {
		if (eventHandler != null) {
			removeHandler(eventHandlersWithQueue, eventHandler);
			removeHandler(eventHandlersWithWait, eventHandler);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.edg.common.event.EventManager#getNumberOfSubscribers()
	 */
	public int getNumberOfSubscribers() {
		return getNumberOfQueueSubscribers() + getNumberOfWaitSubscribers();
	}

	/**
	 * Okidanje dogadjaja.<br>
	 * Dogadjaj se odmah prosledjuje handlerima i poziv metode blokira dok svi handler-i koji su zahtevali takav nacin
	 * obrade ne zavrse sa radom. Nakon toga se dogadjaj stavlja na queue kako bi ga obradjivali handler-i koji nizu
	 * zahtevali cekanje od strane inicijatora dogadjaja.
	 * 
	 * @param dispatchMethod Metode koja se koristi za obradu ovog Event-a
	 * @param dispatchMethodParams Parametri koji se prosledjuju priliko obrade Event-a
	 * @throws Throwable Ukoliko je doslo do greske prilikom obrade dogadjaja
	 */
	public Object invoke(Object proxy, Method dispatchMethod, Object[] dispatchMethodParams) throws Throwable {
		eventDispatchManager.dispatch(dispatchMethod, dispatchMethodParams, getEventHandlers(eventHandlersWithWait),
				getEventHandlers(eventHandlersWithQueue));
		return null;
	}

	/**
	 * Vraca broj subscribera koji su se pretplatili da primaju poruke preko reda cekanja
	 * 
	 * @return Broj queue subscribera
	 */
	int getNumberOfQueueSubscribers() {
		synchronized (eventHandlersWithQueue) {
			return eventHandlersWithQueue.size();
		}
	}

	/**
	 * Vraca broj subscribera koji su se pretplatili da primaju bez cekanja
	 * 
	 * @return Broj subscribera koji ne koriste queue
	 */
	int getNumberOfWaitSubscribers() {
		synchronized (eventHandlersWithWait) {
			return eventHandlersWithWait.size();
		}
	}

	/**
	 * Postavlja eventHandlerInterface i proverava da li je ispravan
	 * 
	 * @throws EventManagerException ukoliko nije ispravan eventHandlerInterface
	 */
	private void setEventHandlerInterface(Class<?> eventHandlerInterface) throws EventManagerException {
		if (eventHandlerInterface == null)
			throw new EventManagerException("Unable tu create EventManager, event handler interface is null");

		if (!eventHandlerInterface.isInterface()) {
			throw new EventManagerException("Unable tu create EventManager, invalid event handler interface");
		}

		this.eventHandlerInterface = eventHandlerInterface;
	}

	/**
	 * Metoda odredjuje listu handlera koji su zaduzeni za obradu event-a.
	 * 
	 * @param eventHandlers Lista raspolozivih handler-a
	 * @return Lista EventHandler-a koji su zaduzeni za obradu Event-a
	 */
	@SuppressWarnings("unchecked")
	private LinkedList<Object> getEventHandlers(LinkedList<Object> eventHandlers) {
		LinkedList<Object> returnValue = null;

		// Odredjujemo koji handler-i ce biti zaduzeni za
		// dispatch ovog event-a
		synchronized (eventHandlers) {
			returnValue = (LinkedList<Object>) eventHandlers.clone();
		}

		return returnValue;
	}

	/**
	 * Pronalazi u listi trazenog eventHandlera i izbacuje ga. Ne koristimo equals, prihvatamo samo istu referencu.
	 * 
	 * @param eventHandlers Spisak hendlera
	 * @param eventHandler Hendler koga izbacujemo.
	 */
	private void removeHandler(LinkedList<Object> eventHandlers, Object eventHandler) {
		synchronized (eventHandlers) {
			for (Iterator<Object> i = eventHandlers.iterator(); i.hasNext();)
				if (i.next() == eventHandler)
					i.remove();
		}
	}

}
