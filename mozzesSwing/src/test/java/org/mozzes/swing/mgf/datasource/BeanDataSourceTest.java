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

import static org.junit.Assert.*;

import org.junit.Test;
import org.mozzes.swing.mgf.DummyClass;
import org.mozzes.swing.mgf.datasource.BeanDataSource;
import org.mozzes.swing.mgf.datasource.DataSource;
import org.mozzes.swing.mgf.datasource.events.DataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.DataSourceEventListener;
import org.mozzes.swing.mgf.datasource.events.SourceChangedEvent;
import org.mozzes.swing.mgf.datasource.impl.DefaultBeanDataSource;


public class BeanDataSourceTest {
	private BeanDataSource<DummyClass> dataSource = null;
	private final DummyClass dataBean = new DummyClass();
	private int handleCalled;

	@Test
	public void testGetData() {
		dataSource = new DefaultBeanDataSource<DummyClass>(DummyClass.class, dataBean);
		assertEquals(dataBean, dataSource.getData());
	}

	@Test
	public void testSetData() {
		// Std. bean dataSource
		handleCalled = 0;
		dataSource = new DefaultBeanDataSource<DummyClass>(DummyClass.class, dataBean);
		dataSource.addEventListener(new Handler());
		dataSource.setData(new DummyClass(10));
		assertEquals(1, handleCalled);
		assertEquals(new DummyClass(10), dataSource.getData());
		// This test emphasises that the source object will not be changed, since we
		// didn't change its data, but have set a NEW SOURCE to the DataSource
		assertEquals(new DummyClass(), dataBean);

		handleCalled = 0;
		dataSource = new DefaultBeanDataSource<DummyClass>(DummyClass.class, dataBean);
		dataSource.addEventListener(new Handler());
		dataSource = new DefaultBeanDataSource<DummyClass>(dataSource);
		dataSource.addEventListener(new Handler());
		dataSource.setData(new DummyClass(11));
		assertEquals(1, handleCalled);
		assertEquals(new DummyClass(11), dataSource.getData());
		// This test emphasizes that the source object will not be changed, since we
		// didn't change its data, but have set a NEW SOURCE to the DataSource
		assertEquals(new DummyClass(), dataBean);

	}

	private class Handler implements DataSourceEventListener<DummyClass> {

		@Override
		public void handleDataSourceEvent(DataSource<DummyClass> source, DataSourceEvent<DummyClass> event) {
			if (event instanceof SourceChangedEvent<?>) {
				handleCalled++;
			}
		}
	}
}
