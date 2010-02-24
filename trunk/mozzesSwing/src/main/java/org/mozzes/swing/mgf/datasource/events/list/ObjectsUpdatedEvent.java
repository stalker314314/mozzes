package org.mozzes.swing.mgf.datasource.events.list;

import java.util.Collection;

/**
 * Event which indicates that an object from ListDataSource was replaced with another one
 * 
 * @author milos
 * 
 * @param <T> The type of the object which DataSource provides
 */
public class ObjectsUpdatedEvent<T> extends ListDataSourceEvent<T> {
	private final Collection<T> objects;

	/**
	 * @param objects Objects that were updated
	 */
	public ObjectsUpdatedEvent(Collection<T> objects) {
		this.objects = objects;
	}

	/**
	 * @return Objects that were updated
	 */
	public Collection<T> getObjects() {
		return objects;
	}
}
