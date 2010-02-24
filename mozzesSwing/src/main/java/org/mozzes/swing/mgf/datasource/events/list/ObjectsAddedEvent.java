package org.mozzes.swing.mgf.datasource.events.list;

import java.util.Collection;

/**
 * Event which indicates that objects were added to ListDataSource
 * 
 * @author milos
 * 
 * @param <T> The type of the object which DataSource provides
 */
public class ObjectsAddedEvent<T> extends ListDataSourceEvent<T> {
	private final Collection<T> objects;
	private final int index;

	/**
	 * @param index Index at which the objects were added
	 * @param objects Objects which were added
	 */
	public ObjectsAddedEvent(int index, Collection<T> objects) {
		this.objects = objects;
		this.index = index;
	}

	/**
	 * @return Objects which were added
	 */
	public Collection<T> getObjects() {
		return objects;
	}

	/**
	 * @return Index at wich the objects were added
	 */
	public int getIndex() {
		return index;
	}

}
