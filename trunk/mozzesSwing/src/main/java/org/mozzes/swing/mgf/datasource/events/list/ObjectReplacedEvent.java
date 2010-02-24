package org.mozzes.swing.mgf.datasource.events.list;

/**
 * Event which indicates that an object from ListDataSource was replaced with another one
 * 
 * @author milos
 * 
 * @param <T> The type of the object which DataSource provides
 */
public class ObjectReplacedEvent<T> extends ListDataSourceEvent<T> {
	private final T replaced;
	private final T with;
	private final int index;

	/**
	 * @param index Index at which the replace was conducted
	 * @param replaced Old object which was replaced
	 * @param with New object which has replaced the old one
	 */
	public ObjectReplacedEvent(int index, T replaced, T with) {
		this.replaced = replaced;
		this.with = with;
		this.index = index;
	}

	/**
	 * @return Old object which was replaced
	 */
	public T getReplaced() {
		return replaced;
	}

	/**
	 * @return New object which has replaced the old one
	 */
	public T getWith() {
		return with;
	}

	/**
	 * @return Index at which the replace was conducted
	 */
	public int getIndex() {
		return index;
	}
}
