package org.mozzes.event.dispatcher;

/**
 * Default implementacija EventDispatcher-a koja samo prosledi dogadjaj handleru
 * 
 * @author Perica Milosevic
 * @version 1.8.2
 * @see EventDispatcher
 */
public class DefaultEventDispatcher implements EventDispatcher {

	public void dispatch(EventDispatchData eventDispatchData) throws Throwable {
		eventDispatchData.dispatch();
	}

}