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