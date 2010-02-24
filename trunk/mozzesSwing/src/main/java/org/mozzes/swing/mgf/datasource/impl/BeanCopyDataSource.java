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