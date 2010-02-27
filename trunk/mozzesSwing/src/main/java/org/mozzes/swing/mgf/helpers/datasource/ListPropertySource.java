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

import org.mozzes.swing.mgf.datamodel.Field;
import org.mozzes.swing.mgf.datamodel.fields.PropertyField;
import org.mozzes.swing.mgf.datasource.BeanDataSource;
import org.mozzes.swing.mgf.datasource.DataSource;
import org.mozzes.swing.mgf.datasource.SelectionListDataSource;
import org.mozzes.swing.mgf.datasource.events.DataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.DataSourceEventListener;
import org.mozzes.swing.mgf.datasource.impl.DefaultSelectionListDataSource;


/**
 * Helper {@link DataSource} commonly used with master detail pattern<br>
 * Converts a {@link Field} whose value for some bean is a list to a {@link SelectionListDataSource}
 * 
 * @author milos
 * 
 * @param <T> Type of the bean
 * @param <F> Type of an element in the list
 */
public class ListPropertySource<T, F> extends DefaultSelectionListDataSource<F> {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unchecked")
	private final Field<T, List> field;
	private final BeanDataSource<T> beanSource;
	private DataSourceEventListener<T> fromBeanSource = new FromBeanSource();

	@SuppressWarnings("unchecked")
	public ListPropertySource(Class<F> elementType, BeanDataSource<T> beanSource, String property) {
		this(elementType, beanSource, new PropertyField<T, List>(List.class, property));
	}

	@SuppressWarnings("unchecked")
	public ListPropertySource(Class<F> elementType, BeanDataSource<T> beanSource, Field<T, List> field) {
		super(elementType);

		this.beanSource = beanSource;
		this.field = field;

		super.setData(getFieldValue());

		beanSource.addEventListener(fromBeanSource);
	}

	@SuppressWarnings("unchecked")
	private List<F> getFieldValue() {
		return field.getValue(beanSource.getData());
	}

	private class FromBeanSource implements DataSourceEventListener<T> {
		@Override
		public void handleDataSourceEvent(DataSource<T> source, DataSourceEvent<T> event) {
			ListPropertySource.this.setData(getFieldValue());
		}
	}
}
