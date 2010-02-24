package org.mozzes.event.invoker;

/**
 * Servis koji omogucava okidanje dogadjaja u sistemu. Svako ko zeli okidanje dogadjaja mora ga okinuti koriscenjem ovog
 * interfejsa
 * 
 * @author Perica Milosevic
 * @version 1.8.2
 */
public interface EventInvokerService<T> {
	static final String SERVICE_TYPE = "eventInvoker://";

	/**
	 * Vraca objekat preko koga se iniciraju dogadjaji
	 * 
	 * @return Objekat preko koga se iniciraju dogadjaji
	 * @throws EventInvokerException Ukoliko nije moguce okidanje dogadjaja
	 */
	public T getInvoker() throws EventInvokerException;

}