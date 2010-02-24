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
