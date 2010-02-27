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
package org.mozzes.swing.mgf.datasource.events;

import org.mozzes.swing.mgf.datasource.DataSource;

/**
 * Interface which has be implemented in order to listen all DataSource events
 * 
 * @author milos
 * 
 * @param <T> The type of the object which DataSource provides
 */
public interface DataSourceEventListener<T> {
	/**
	 * Implement to handle any {@link DataSourceEvent}
	 * 
	 * @param source {@link DataSource Data source} which fired the event
	 * @param event {@link DataSourceEvent Event} which was fired
	 */
	public void handleDataSourceEvent(DataSource<T> source, DataSourceEvent<T> event);
}
