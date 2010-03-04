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
package org.mozzes.event.invoker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Klasa koja kreira proxy preko koga se okidaju dogadjaji i prosledjuju odgovarajucem EventManageru kako bi svi
 * prijavljeni handleri dobili obavestenje.
 * 
 * @author Perica Milosevic
 * @version 1.8.2
 * @see EventInvokerService
 */
public class EventInvoker<T> implements EventInvokerService<T> {

	/**
	 * Proxy koji hvata sve pozive metoda iz handler interfejsa i prosledjuje ih odgovarajucem InvocationHandler-u
	 */
	private T eventHandlerProxy;

	@SuppressWarnings("unchecked")
	public EventInvoker(Class<?> eventHandlerInterface, InvocationHandler handler) throws EventInvokerException {
		if (eventHandlerInterface == null)
			throw new EventInvokerException("Unable tu create EventInvoker, event handler interface is null");

		if (!eventHandlerInterface.isInterface()) {
			throw new EventInvokerException("Unable tu create EventInvoker, invalid event handler interface");
		}

		eventHandlerProxy = (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
				new Class[] { eventHandlerInterface }, handler);
	}

	public T getInvoker() {
		return eventHandlerProxy;
	}

}