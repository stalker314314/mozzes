package org.mozzes.swing.mgf.datamodel.events.field;

/**
 * Indicates that field value for an object has changed
 * 
 * @author milos
 * 
 * @param <T> The type of object on which the field operates
 * @param <F> The type of the field value
 */
public class ValueChangedEvent<T, F> extends FieldEvent<T, F> {
	private final F from;
	private final F to;
	private final T forObject;

	/**
	 * @param forObject Object for which the field was updated
	 * @param from Previous value of the field for specified object (<i>forObject</i>)
	 * @param to Current value of the field for specified object (<i>forObject</i>)
	 */
	public ValueChangedEvent(T forObject, F from, F to) {
		this.from = from;
		this.to = to;
		this.forObject = forObject;
	}

	/**
	 * @return Previous value of the field for specified object (<i>forObject</i>)
	 */
	public F getFrom() {
		return from;
	}

	/**
	 * @return Current value of the field for specified object (<i>forObject</i>)
	 */
	public F getTo() {
		return to;
	}

	/**
	 * @return Object for which the field was updated
	 */
	public T getForObject() {
		return forObject;
	}
}
