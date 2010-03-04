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

import org.mozzes.swing.mgf.datasource.DataSource;
import org.mozzes.utils.JavaUtils;


public class BeanCopyDataSource<T> extends DefaultBeanDataSource<T> {
	private static final long serialVersionUID = 1L;

	public BeanCopyDataSource(Class<T> dataType) {
		super(dataType);
	}

	public BeanCopyDataSource(Class<T> dataType, T dataBean) {
		super(dataType, dataBean);
	}

	public BeanCopyDataSource(DataSource<T> dataSource) {
		super(dataSource);
	}

	@Override
	public void setData(T data) {
		if (data == null) {
			super.setData(null);
			return;
		}

		T copiedData = JavaUtils.getDeepCopy(data);
		super.setData(copiedData);
	}
}