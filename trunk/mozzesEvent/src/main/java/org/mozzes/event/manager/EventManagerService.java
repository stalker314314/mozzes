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
