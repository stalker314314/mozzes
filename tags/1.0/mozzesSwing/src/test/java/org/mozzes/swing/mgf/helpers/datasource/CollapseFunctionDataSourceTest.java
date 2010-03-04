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

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.mozzes.swing.mgf.DummyClass;
import org.mozzes.swing.mgf.datasource.ListDataSource;
import org.mozzes.swing.mgf.datasource.impl.DefaultListDataSource;
import org.mozzes.swing.mgf.helpers.datasource.CollapseFunctionDataSource;
import org.mozzes.swing.mgf.helpers.datasource.CollapseFunctionDataSource.CollapseFunction;


public class CollapseFunctionDataSourceTest {

	@Test
	public void testFunctionality() {
		ListDataSource<DummyClass> source = new DefaultListDataSource<DummyClass>(DummyClass.class);
		CollapseFunctionDataSource<DummyClass> collapse = new CollapseFunctionDataSource<DummyClass>(source,
				new CollapseFunction<DummyClass>() {
			@Override
			public DummyClass function(List<DummyClass> data) {
				if (data == null || data.isEmpty())
					return null;
				DummyClass dummy = new DummyClass();
				dummy.setIntAttr(0);

				for (DummyClass value : data) {
					dummy.setDoubleValue(dummy.getDoubleValue() + value.getDoubleValue());
					dummy.setIntAttr(dummy.getIntAttr() + value.getIntAttr());
				}

				return dummy;
			}
		});

		double delta = 0.000000000000001;
		assertEquals(null, collapse.getData());

		source.add(new DummyClass().setDoubleValue(10).setIntAttr(1));
		assertEquals(10, collapse.getData().getDoubleValue(), delta);
		// assertEquals(Integer.valueOf(1), collapse.getData().getIntAttr());

		source.add(new DummyClass().setDoubleValue(20).setIntAttr(2));
		assertEquals(30, collapse.getData().getDoubleValue(), delta);
		// assertEquals(Integer.valueOf(2), collapse.getData().getIntAttr());

		source.add(new DummyClass().setDoubleValue(30).setIntAttr(3));
		assertEquals(60, collapse.getData().getDoubleValue(), delta);
		// assertEquals(Integer.valueOf(3), collapse.getData().getIntAttr());

		source.remove(new DummyClass().setDoubleValue(30).setIntAttr(2));
		assertEquals(40, collapse.getData().getDoubleValue(), delta);

	}
}