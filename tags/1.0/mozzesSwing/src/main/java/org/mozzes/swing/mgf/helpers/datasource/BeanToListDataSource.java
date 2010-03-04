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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.mozzes.swing.mgf.datasource.BeanDataSource;
import org.mozzes.swing.mgf.datasource.DataSource;
import org.mozzes.swing.mgf.datasource.ListDataSource;
import org.mozzes.swing.mgf.datasource.events.DataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.bean.BeanDataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.bean.BeanDataSourceEventListener;
import org.mozzes.swing.mgf.datasource.impl.DefaultSelectionListDataSource;
import org.mozzes.swing.mgf.datasource.impl.SelectionMode;


/**
 * Helper {@link DataSource} that allows a {@link BeanDataSource} to be represented as unmodifiable
 * {@link ListDataSource} with one value (or 0 if {@link BeanDataSource} is empty)
 * 
 * @author milos
 * 
 * @param <T> Type of the bean
 */
public class BeanToListDataSource<T> extends DefaultSelectionListDataSource<T> {
	private static final long serialVersionUID = 1L;
	private final DataSource<T> beanSource;

	/**
	 * @param beanSource {@link DataSource Source} that is to be presented as {@link ListDataSource}
	 */
	public BeanToListDataSource(DataSource<T> beanSource) {
		super(beanSource.getDataType());
		this.beanSource = beanSource;
		beanSource.addEventListener(new Synchronizer());
		super.setSelectionMode(SelectionMode.SINGLE_SELECTION);
		super.setData(new ArrayList<T>());
		syncSources();
	}

	private void syncSources() {
		if (beanSource.getData() == null) {
			super.clear();
		} else if (getSize() == 0) {
			ArrayList<T> elements = new ArrayList<T>();
			elements.add(beanSource.getData());
			super.add(elements);
		} else if (getSize() == 1) {
			super.set(0, beanSource.getData());
			this.fireDataUpdatedEvent();
		}
	}

	@Override
	public void add(Collection<T> elements) {
		throw new UnsupportedOperationException("This operation is not allowed!");
	}

	@Override
	public void add(int index, Collection<T> elements) {
		throw new UnsupportedOperationException("This operation is not allowed!");
	}

	@Override
	public void add(int index, T... elements) {
		throw new UnsupportedOperationException("This operation is not allowed!");
	}

	@Override
	public void add(T object) {
		throw new UnsupportedOperationException("This operation is not allowed!");
	}

	@Override
	public void add(int index, T element) {
		throw new UnsupportedOperationException("This operation is not allowed!");
	};

	@Override
	public void add(T... elements) {
		throw new UnsupportedOperationException("This operation is not allowed!");
	}

	@Override
	public T set(int index, T element) {
		throw new UnsupportedOperationException("This operation is not allowed!");
	}

	@Override
	public void setData(List<T> data) {
		throw new UnsupportedOperationException("This operation is not allowed!");
	}

	@Override
	public void setSelectionMode(SelectionMode mode) {
		throw new UnsupportedOperationException("This operation is not allowed!");
	}

	@Override
	public boolean remove(Collection<T> elements) {
		throw new UnsupportedOperationException("This operation is not allowed!");
	}

	@Override
	public T remove(int index) {
		throw new UnsupportedOperationException("This operation is not allowed!");
	}

	@Override
	public boolean remove(T object) {
		throw new UnsupportedOperationException("This operation is not allowed!");
	}

	@Override
	public boolean remove(T... elements) {
		throw new UnsupportedOperationException("This operation is not allowed!");
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("This operation is not allowed!");
	}

	private class Synchronizer implements BeanDataSourceEventListener<T> {

		@Override
		public void handleDataSourceEvent(BeanDataSource<T> source, BeanDataSourceEvent<T> event) {
			syncSources();
		}

		@Override
		public void handleDataSourceEvent(DataSource<T> source, DataSourceEvent<T> event) {
			syncSources();
		}
	}
}
