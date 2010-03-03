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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mozzes.swing.mgf.DummyClass;
import org.mozzes.swing.mgf.datasource.AbstractDataSource;
import org.mozzes.swing.mgf.datasource.DataSource;
import org.mozzes.swing.mgf.datasource.events.DataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.DataSourceEventListener;
import org.mozzes.swing.mgf.datasource.events.SourceChangedEvent;


public class AbstractDataSourceTest {
	private AbstractDataSource<DummyClass> dataSource = null;
	private boolean handleCalled = false;

	@Test
	public void testAddEventListener() {
		dataSource = new AbstractDataSource<DummyClass>(DummyClass.class, new DummyClass()) {
			private static final long serialVersionUID = 15L;

			@Override
			protected List<Class<?>> getSupportedBaseEvents() {
				List<Class<?>> events = new ArrayList<Class<?>>();
				events.add(DataSourceEvent.class);
				return events;
			}

		};
		dataSource.addEventListener(new Handler());

		try {
			dataSource.addEventListener(null);
			fail("IllegalArgumentException has to be thrown");
		} catch (IllegalArgumentException ok) {
		}
	}

	@Test
	public void testRemoveEventListener() {
		dataSource = new AbstractDataSource<DummyClass>(DummyClass.class, new DummyClass()) {
			private static final long serialVersionUID = 15L;

			@Override
			protected List<Class<?>> getSupportedBaseEvents() {
				List<Class<?>> events = new ArrayList<Class<?>>();
				events.add(DataSourceEvent.class);
				return events;
			}
		};
		dataSource.removeEventListener(new Handler());
	}

	@Test
	public void testGetData() {
		dataSource = new AbstractDataSource<DummyClass>(DummyClass.class, new DummyClass()) {
			private static final long serialVersionUID = 15L;

			@Override
			protected List<Class<?>> getSupportedBaseEvents() {
				List<Class<?>> events = new ArrayList<Class<?>>();
				events.add(DataSourceEvent.class);
				return events;
			}
		};
		assertEquals(new DummyClass(), dataSource.getData());
		dataSource = new AbstractDataSource<DummyClass>(DummyClass.class, dataSource) {
			private static final long serialVersionUID = 15L;

			@Override
			protected List<Class<?>> getSupportedBaseEvents() {
				List<Class<?>> events = new ArrayList<Class<?>>();
				events.add(DataSourceEvent.class);
				return events;
			}
		};
		assertEquals(new DummyClass(), dataSource.getData());
	}

	@Test
	public void testSetData() {
		dataSource = new AbstractDataSource<DummyClass>(DummyClass.class, new DummyClass()) {
			private static final long serialVersionUID = 15L;

			@Override
			protected List<Class<?>> getSupportedBaseEvents() {
				List<Class<?>> events = new ArrayList<Class<?>>();
				events.add(DataSourceEvent.class);
				return events;
			}
		};
		dataSource.addEventListener(new Handler());
		dataSource.setData(new DummyClass(10));
		assertEquals(new DummyClass(10), dataSource.getData());
		assertTrue(handleCalled);
		dataSource = new AbstractDataSource<DummyClass>(DummyClass.class, dataSource) {
			private static final long serialVersionUID = 15L;

			@Override
			protected List<Class<?>> getSupportedBaseEvents() {
				List<Class<?>> events = new ArrayList<Class<?>>();
				events.add(DataSourceEvent.class);
				return events;
			}
		};
		handleCalled = false;
		dataSource.setData(new DummyClass(11));
		assertFalse(handleCalled); // Zato sto je handler i dalje prikacen na stari DS koji se nije izmenio
		assertEquals(new DummyClass(11), dataSource.getData());
	}

	@Test
	public void testFireEvent() {
		dataSource = new AbstractDataSource<DummyClass>(DummyClass.class, new DummyClass()) {
			private static final long serialVersionUID = 15L;

			@Override
			protected List<Class<?>> getSupportedBaseEvents() {
				List<Class<?>> events = new ArrayList<Class<?>>();
				events.add(DataSourceEvent.class);
				return events;
			}
		};
		dataSource.addEventListener(new Handler());
		try {
			dataSource.fireEvent(null);
		} catch (IllegalArgumentException ok) {
		}
		assertFalse(handleCalled);
		dataSource.fireEvent(new SourceChangedEvent<DummyClass>(dataSource.getData(), null));
		assertTrue(handleCalled);
		handleCalled = false;
	}

	@Test
	public void testBindTo() {
		DummyClass data = new DummyClass();
		dataSource = new AbstractDataSource<DummyClass>(DummyClass.class, data) {
			private static final long serialVersionUID = 15L;

			@Override
			protected List<Class<?>> getSupportedBaseEvents() {
				List<Class<?>> events = new ArrayList<Class<?>>();
				events.add(DataSourceEvent.class);
				return events;
			}
		};
		assertEquals(data, dataSource.getData());
		dataSource.getData().setIntAttr(1);
		assertEquals(data, dataSource.getData());
		dataSource.addEventListener(new Handler());
		try {
			dataSource.bindTo(dataSource);
			fail("Should throw IllegalArgumentException since setting" +
					"itself as a source causes infinite loop");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		data.setIntAttr(123);
		AbstractDataSource<DummyClass> dataSource2 = new AbstractDataSource<DummyClass>(DummyClass.class, data) {
			private static final long serialVersionUID = 15L;

			@Override
			protected List<Class<?>> getSupportedBaseEvents() {
				List<Class<?>> events = new ArrayList<Class<?>>();
				events.add(DataSourceEvent.class);
				return events;
			}
		};
		dataSource2.addEventListener(new Handler());
		dataSource.bindTo(dataSource2);
		dataSource.addEventListener(new Handler());
		assertEquals(new DummyClass(123), dataSource.getData());
		dataSource.getData().setIntAttr(111);
		assertEquals(new DummyClass(111), dataSource.getData());

		handleCalled = false;
		dataSource.setData(new DummyClass(11));
		assertTrue(handleCalled);
		assertEquals(new DummyClass(11), dataSource.getData());
		// This test emphasises that the source object will not be changed, since we
		// didn't change its data, but have set a NEW SOURCE to the DataSource
		assertEquals(new DummyClass(111), data);
	}

	private class Handler implements DataSourceEventListener<DummyClass> {

		@Override
		public void handleDataSourceEvent(DataSource<DummyClass> source, DataSourceEvent<DummyClass> event) {
			if (event instanceof SourceChangedEvent<?>) {
				handleSourceChanged((SourceChangedEvent<DummyClass>) event);
			}
		}

		private void handleSourceChanged(SourceChangedEvent<DummyClass> event) {
			handleCalled = true;
		}
	}
}
