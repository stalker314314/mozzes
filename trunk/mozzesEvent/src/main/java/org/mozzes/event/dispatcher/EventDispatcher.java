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
