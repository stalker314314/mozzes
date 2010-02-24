package org.mozzes.swing.mgf.datasource;

import org.mozzes.swing.mgf.datasource.events.DataSourceEventListener;
import org.mozzes.swing.mgf.datasource.events.bean.DataUpdatedEvent;

/**
 * Base data source interface supporting only change of the provided object.<br>
 * More sophisticated data source interfaces extend it.
 * 
 * @author milos
 * 
 * @param <T> Type of the bean that is provided by the data source
 */
public interface DataSource<T> {
	/**
	 * Fire {@link DataUpdatedEvent} manually<br>
	 * Use this when you change the data provided by this source through code
	 */
	void fireDataUpdatedEvent();

	/**
	 * Adds a listener to the list that's notified each time a change to the data provided by this source occurs.
	 * 
	 * @param listener The listener to be notified about model events
	 */
	void addEventListener(DataSourceEventListener<T> listener);

	/**
	 * Removes a listener from the list that's notified each time a change to the data provided by this source occurs.
	 * 
	 * @param listener The listener to be removed
	 */
	void removeEventListener(DataSourceEventListener<T> listener);

	/**
	 * @return Data provided by this {@link DataSource}
	 */
	T getData();

	/**
	 * Sets the data which will be provided by this {@link DataSource}
	 * 
	 * @param data Data that should be provided
	 */
	public void setData(T data);

	/**
	 * Sets the {@link DataSource} from which the data will be provided
	 * 
	 * @param dataSource DataSource from which the data should be provided
	 */
	void bindTo(DataSource<T> dataSource);

	/**
	 * @return Type of a bean provided by this data source
	 */
	Class<T> getDataType();
}
