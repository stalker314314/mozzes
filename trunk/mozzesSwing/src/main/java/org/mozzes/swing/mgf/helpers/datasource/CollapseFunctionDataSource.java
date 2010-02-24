package org.mozzes.swing.mgf.helpers.datasource;

import java.util.List;

import org.mozzes.swing.mgf.datasource.BeanDataSource;
import org.mozzes.swing.mgf.datasource.DataSource;
import org.mozzes.swing.mgf.datasource.ListDataSource;
import org.mozzes.swing.mgf.datasource.events.DataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.DataSourceEventListener;
import org.mozzes.swing.mgf.datasource.impl.DefaultBeanDataSource;


/**
 * {@link DataSource} which using some {@link CollapseFunction function} collapses whole {@link ListDataSource list} of
 * objects to a single {@link BeanDataSource bean}
 * 
 * @author milos
 * 
 * @param <T> Type of the bean
 */
public class CollapseFunctionDataSource<T> extends DefaultBeanDataSource<T> {
	private static final long serialVersionUID = 1L;
	private final CollapseFunction<T> collapse;
	private final ListDataSource<T> listSource;

	/**
	 * @param listSource {@link ListDataSource Source} to be collapsed
	 * @param collapse {@link CollapseFunction Function} that does the work
	 */
	public CollapseFunctionDataSource(ListDataSource<T> listSource, CollapseFunction<T> collapse) {
		super(listSource.getElementType());
		this.listSource = listSource;
		this.collapse = collapse;
		recalculate();
		listSource.addEventListener(new DataSourceEventListener<List<T>>() {
			@Override
			public void handleDataSourceEvent(DataSource<List<T>> source, DataSourceEvent<List<T>> event) {
				recalculate();
			}
		});
	}

	@Override
	public void setData(T data) {
		throw new UnsupportedOperationException("This operation is not available!");
	}

	@Override
	public void bindTo(DataSource<T> dataSource) {
		throw new UnsupportedOperationException("This operation is not available!");
	}

	private void recalculate() {
		super.setData(collapse.function(listSource.getData()));
	}

	@Override
	protected void fireEvent(DataSourceEvent<T> event) {
		super.fireEvent(event);
	}

	public static interface CollapseFunction<T> {
		public T function(List<T> data);
	}
}
