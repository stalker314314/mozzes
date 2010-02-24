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
