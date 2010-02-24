package org.mozzes.swing.mgf.datasource;

import java.util.Collection;
import java.util.List;

import org.mozzes.swing.mgf.datasource.events.list.ObjectsUpdatedEvent;


/**
 * Provides a list of beans, and allows list manipulation event notifications about changes
 * 
 * @author milos
 * 
 * @param <T> Type of the bean contained by the list
 */
public interface ListDataSource<T> extends DataSource<List<T>> {

	/**
	 * @param index Index from which to get the bean
	 * @return Bean from the list at specified <i>index</i>
	 */
	public T get(int index);

	/**
	 * @param object Object that should be added to the list provided by this source
	 */
	public void add(T object);

	/**
	 * @param objects Objects that should be added to the list provided by this source
	 */
	public void add(T... objects);

	/**
	 * @param objects Objects that should be added to the list provided by this source
	 */
	public void add(Collection<T> objects);

	/**
	 * Adds object starting at specified <i>index</i>
	 * 
	 * @param index Index at which to start adding
	 * @param object Object that should be added to the list provided by this source
	 */
	public void add(int index, T object);

	/**
	 * Adds objects starting at specified <i>index</i>
	 * 
	 * @param index Index at which to start adding
	 * @param objects Objects that should be added to the list provided by this source
	 */
	public void add(int index, T... objects);

	/**
	 * Adds objects starting at specified <i>index</i>
	 * 
	 * @param index Index at which to start adding
	 * @param objects Objects that should be added to the list provided by this source
	 */
	public void add(int index, Collection<T> objects);

	/**
	 * Replaces an object at specified <i>index</i> with the provided <i>object</i>
	 * 
	 * @param index Index at which to conduct replacement
	 * @param object Object to use as a replacement
	 * @return Object that was replaced
	 */
	public T set(int index, T object);

	/**
	 * Removes an object at specified <i>index</i>
	 * 
	 * @param index Index at which to conduct removal
	 * @return Object that was removed
	 */
	public T remove(int index);

	/**
	 * Removes the specified <i>object</i> from the list provided by this source
	 * 
	 * @param object Object that should be removed
	 * @return true if the object was removed, false if it was not in the list
	 */
	public boolean remove(T object);

	/**
	 * Removes specified <i>objects</i> from the list provided by this source
	 * 
	 * @param objects Objects that should be removed
	 * @return true if the list was changed, false otherwise
	 */
	public boolean remove(T... objects);

	/**
	 * Removes specified <i>objects</i> from the list provided by this source
	 * 
	 * @param objects Objects that should be removed
	 * @return true if the list was changed, false otherwise
	 */
	public boolean remove(Collection<T> objects);

	/**
	 * Clears the list (removes all elements from it)
	 */
	public void clear();

	/**
	 * @return Number of objects in the list provided by the source
	 */
	public int getSize();

	/**
	 * Fires an {@link ObjectsUpdatedEvent} for all objects in the data source<br>
	 * <b>Use this method after you have done changing the data directly from code to allow binded GUI component to
	 * refresh</b>
	 */
	public void fireObjectsUpdatedEvent();

	/**
	 * Fires an {@link ObjectsUpdatedEvent} for the specified <b>object</b> in the data source<br>
	 * <b>Use this method after you have done changing the data directly from code to allow binded GUI component to
	 * refresh</b>
	 * 
	 * @param object Object that was updated
	 */
	public void fireObjectsUpdatedEvent(T object);

	/**
	 * Fires an {@link ObjectsUpdatedEvent} for specified <b>objects</b> in the data source<br>
	 * <b>Use this method after you have done changing the data directly from code to allow binded GUI component to
	 * refresh</b>
	 * 
	 * @param objects Objects that were updated
	 */
	public void fireObjectsUpdatedEvent(T... objects);

	/**
	 * Fires an {@link ObjectsUpdatedEvent} for specified <b>objects</b> in the data source<br>
	 * <b>Use this method after you have done changing the data directly from code to allow binded GUI component to
	 * refresh</b>
	 * 
	 * @param objects Objects that were updated
	 */
	public void fireObjectsUpdatedEvent(Collection<T> objects);

	/**
	 * @param object Object to be found
	 * @return Index of the specified <i>object</i> or -1 if <i>object</i> is not in the list
	 */
	public int indexOf(T object);

	
	/**
	 * @return Type of a bean contained by the list
	 */
	Class<T> getElementType();
}
