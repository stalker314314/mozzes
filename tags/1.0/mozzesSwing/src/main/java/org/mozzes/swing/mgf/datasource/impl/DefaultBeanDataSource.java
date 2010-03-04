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
package org.mozzes.swing.mgf.datasource.impl;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.mozzes.swing.mgf.datasource.AbstractDataSource;
import org.mozzes.swing.mgf.datasource.BeanDataSource;
import org.mozzes.swing.mgf.datasource.DataSource;
import org.mozzes.swing.mgf.datasource.events.bean.BeanDataSourceEvent;
import org.mozzes.validation.ValidationFactory;


/**
 * Default implementation of {@link BeanDataSource}
 * 
 * @author milos
 * 
 * @param <T> Type of the bean which is provided
 */
public class DefaultBeanDataSource<T> extends AbstractDataSource<T> implements BeanDataSource<T> {
	private static final long serialVersionUID = 15L;

	/**
	 * @param dataType Type of the bean that will be provided
	 */
	public DefaultBeanDataSource(Class<T> dataType) {
		super(dataType);
	}

	/**
	 * @param dataType Type of the bean that will be provided
	 * @param dataBean Bean to provide
	 */
	public DefaultBeanDataSource(Class<T> dataType, T dataBean) {
		super(dataType, dataBean);
	}

	/**
	 * @param dataSource DataSource to use as the source provider
	 */
	public DefaultBeanDataSource(DataSource<T> dataSource) {
		super(dataSource.getDataType(), dataSource);
	}

	@Override
	protected List<Class<?>> getSupportedBaseEvents() {
		List<Class<?>> supproted = super.getSupportedBaseEvents();
		supproted.add(BeanDataSourceEvent.class);
		return supproted;
	}

	@Override
	public Set<ConstraintViolation<T>> validate(Class<?>... groups) {
		return ValidationFactory.getValidator().validate(this.getData(), groups);
}
}
