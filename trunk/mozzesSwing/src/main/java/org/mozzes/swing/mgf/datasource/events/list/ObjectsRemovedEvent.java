package org.mozzes.swing.mgf.datasource.events.list;

import java.util.Collection;

/**
 * Event which indicates that objects were removed from ListDataSource
 * 
 * @author milos
 * 
 * @param <T> The type of the object which DataSource provides
 */
public class ObjectsRemovedEvent<T> extends ListDataSourceEvent<T> {
	private final Collection<T> objects;

	/**
	 * @param objects Objects which were removed
	 */
	public ObjectsRemovedEvent(Collection<T> objects) {
		this.objects = objects;
	}

	/**
	 * @return Objects which were removed
	 */
	public Collection<T> getObjects() {
		return objects;
	}
}
