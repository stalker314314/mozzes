package org.mozzes.swing.mgf.datasource.events;

/**
 * Indicates that data source is now providing object which is different from the one provided before
 * 
 * @author milos
 * 
 * @param <T> The type of the object which DataSource provides
 */
public class SourceChangedEvent<T> extends DataSourceEvent<T> {
	private final T from;
	private final T to;

	/**
	 * @param from Old object which was provided by the data source
	 * @param to New object which is provided by the data source
	 */
	public SourceChangedEvent(T from, T to) {
		this.from = from;
		this.to = to;
	}

	/**
	 * @return Old object which was provided by the data source
	 */
	public T getFrom() {
		return from;
	}

	/**
	 * @return New object which is provided by the data source
	 */
	public T getTo() {
		return to;
	}

}
