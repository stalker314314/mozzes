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
package org.mozzes.event;

import org.mozzes.event.configuration.EventConfiguration;
import org.mozzes.event.configuration.EventConfigurationException;
import org.mozzes.event.configuration.EventServiceConfigurator;
import org.mozzes.event.invoker.EventInvokerException;
import org.mozzes.event.invoker.EventInvokerService;
import org.mozzes.event.invoker.EventInvokerServiceLocator;
import org.mozzes.event.manager.EventManagerException;
import org.mozzes.event.manager.EventManagerService;
import org.mozzes.event.manager.EventManagerServiceLocator;
import org.mozzes.event.service.DefaultServiceRegistry;
import org.mozzes.event.service.ServiceRegistry;

/**
 * Glavna klasa za rad sa eventima u sistemu.
 * <p>
 * Ova klasa je pristupna klasa za sve ove delove sistema i ovde se radi:
 * <ul>
 * <li>konfiguracija event-a</li>
 * <li>dobijanje {@link EventManagerService}-a</li>
 * <li>dobijanje {@link EventInvokerService}-a</li>
 * </ul>
 * 
 * <p>
 * 
 * @author Perica Milosevic
 * @version 1.8.2
 * @see EventConfiguration
 */
public class Event extends EventServiceConfigurator {

	private static final Event INSTANCE = new Event(new DefaultServiceRegistry());

	public static Event getInstance() {
		return INSTANCE;
	}

	private ServiceRegistry serviceRegistry;

	protected Event(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	/**
	 * Vraca EventManagerService za rad sa odredjenim tipom dogadjaja
	 * 
	 * @param eventHandlerInterface event interface - tip dogadjaja
	 * @return EventManagerService koji se koristi za pretplatu na dogadjaje
	 * @throws EventManagerException ukoliko nije uspelo dobijanje EventManagerService-a
	 */
	public EventManagerService getManager(Class<?> eventHandlerInterface) throws EventManagerException {
		return EventManagerServiceLocator.lookup(serviceRegistry, eventHandlerInterface);
	}

	/**
	 * Vraca objekat preko koga se mogu okidati dogadjaji u sistemu.
	 * 
	 * @param eventHandlerInterface event interface - tip dogadjaja
	 * @return objekat preko koga se mogu okidati dogadjaji u sistemu.
	 * @throws EventInvokerException ukoliko nije uspelo dobijanje objekta
	 */
	public <T> T getInvoker(Class<T> eventHandlerInterface) throws EventInvokerException {
		return EventInvokerServiceLocator.lookup(serviceRegistry, eventHandlerInterface).getInvoker();
	}

	/**
	 * Konfigurisanje sistema za rad sa eventima
	 * 
	 * @param configuration konfiguracija evenata
	 * @throws EventConfigurationException ukoliko konfiguracija ima gresaka
	 */
	public void configure(EventConfiguration configuration) throws EventConfigurationException {
		EventServiceConfigurator.configure(serviceRegistry, configuration);
	}
}
