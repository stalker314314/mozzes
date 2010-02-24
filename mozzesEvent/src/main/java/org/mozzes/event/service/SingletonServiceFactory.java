package org.mozzes.event.service;

/**
 * Implementacija ServiceFactory-ja koja uvek vraca istu instancu servisa, onu koju je dobila prilikom kreiranja.
 * Koristi se ukoliko nije potrebno kreirati novu instancu fabrike za kreiranje servisa svaki put kada se trazi njeno
 * kreiranje.
 * 
 * @author Perica Milosevic
 * @version 1.8.2
 * @see ServiceFactory
 */
public class SingletonServiceFactory implements ServiceFactory {

	private Object serviceImplementation;

	public SingletonServiceFactory(Object serviceImplementation) {
		this.serviceImplementation = serviceImplementation;
	}

	public Object create() {
		return serviceImplementation;
	}

}
