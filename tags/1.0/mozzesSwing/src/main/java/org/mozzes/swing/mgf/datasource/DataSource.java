/*
 * Copyright 2010 Mozzart
 *
 *
 * This file is part of mozzes.
 *
 * mozzes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mozzes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mozzes.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
