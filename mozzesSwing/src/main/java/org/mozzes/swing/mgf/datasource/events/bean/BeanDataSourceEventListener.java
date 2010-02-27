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
package org.mozzes.swing.mgf.datasource.events.bean;

import org.mozzes.swing.mgf.datasource.BeanDataSource;
import org.mozzes.swing.mgf.datasource.events.DataSourceEventListener;

/**
 * Interface which SHOULD be implemented in order to listen to BeanDataSource events
 * 
 * @author milos
 * 
 * @param <T> The type of the object which DataSource provides
 */
public interface BeanDataSourceEventListener<T> extends DataSourceEventListener<T> {
	/**
	 * Implement to handle any {@link BeanDataSourceEvent}
	 * 
	 * @param source {@link BeanDataSource Data source} which fired the event
	 * @param event {@link BeanDataSourceEvent Event} which was fired
	 */
	public void handleDataSourceEvent(BeanDataSource<T> source, BeanDataSourceEvent<T> event);
}
