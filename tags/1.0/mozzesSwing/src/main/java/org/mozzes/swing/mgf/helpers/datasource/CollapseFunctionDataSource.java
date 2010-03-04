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
package org.mozzes.swing.mgf.helpers.datasource;

import java.util.List;

import org.mozzes.swing.mgf.datasource.BeanDataSource;
import org.mozzes.swing.mgf.datasource.DataSource;
import org.mozzes.swing.mgf.datasource.ListDataSource;
import org.mozzes.swing.mgf.datasource.events.DataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.DataSourceEventListener;
import org.mozzes.swing.mgf.datasource.impl.DefaultBeanDataSource;


/**
 * {@link DataSource} which using some {@link CollapseFunction function} collapses whole {@link ListDataSource list} of
 * objects to a single {@link BeanDataSource bean}
 * 
 * @author milos
 * 
 * @param <T> Type of the bean
 */
public class CollapseFunctionDataSource<T> extends DefaultBeanDataSource<T> {
	private static final long serialVersionUID = 1L;
	private final CollapseFunction<T> collapse;
	private final ListDataSource<T> listSource;

	/**
	 * @param listSource {@link ListDataSource Source} to be collapsed
	 * @param collapse {@link CollapseFunction Function} that does the work
	 */
	public CollapseFunctionDataSource(ListDataSource<T> listSource, CollapseFunction<T> collapse) {
		super(listSource.getElementType());
		this.listSource = listSource;
		this.collapse = collapse;
		recalculate();
		listSource.addEventListener(new DataSourceEventListener<List<T>>() {
			@Override
			public void handleDataSourceEvent(DataSource<List<T>> source, DataSourceEvent<List<T>> event) {
				recalculate();
			}
		});
	}

	@Override
	public void setData(T data) {
		throw new UnsupportedOperationException("This operation is not available!");
	}

	@Override
	public void bindTo(DataSource<T> dataSource) {
		throw new UnsupportedOperationException("This operation is not available!");
	}

	private void recalculate() {
		super.setData(collapse.function(listSource.getData()));
	}

	@Override
	protected void fireEvent(DataSourceEvent<T> event) {
		super.fireEvent(event);
	}

	public static interface CollapseFunction<T> {
		public T function(List<T> data);
	}
}
