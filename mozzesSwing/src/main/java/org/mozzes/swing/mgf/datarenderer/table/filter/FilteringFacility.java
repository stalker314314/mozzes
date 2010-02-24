package org.mozzes.swing.mgf.datarenderer.table.filter;

import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.RowFilter.Entry;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.mozzes.swing.mgf.datarenderer.table.TableRenderModel;
import org.mozzes.utils.filtering.ExpressionFilter;
import org.mozzes.utils.filtering.Filter;


/**
 * Provides methods for setting up cell editors
 * 
 * @author milos
 */
public class FilteringFacility<T> {
	private final TableRenderModel<T> attachedTable;
	private final ExpressionFilter<T> filter = new ExpressionFilter<T>();

	/**
	 * @param table Setups table to work with the facility
	 */
	public FilteringFacility(TableRenderModel<T> table) {
		this.attachedTable = table;
		setRowFilter(convertToRowFilter(filter));
	}

	/**
	 * @return Table attached to this facility instance
	 */
	public TableRenderModel<T> getAttachedTable() {
		return attachedTable;
	}

	/**
	 * Sets and applies the passed {@link Filter}
	 * 
	 * @param filter {@link Filter} to be set
	 */
	public void setFilter(Filter<T> filter) {
		if (filter == null)
			this.filter.deleteWrappedFilter();
		else
			this.filter.setWrappedFilter(filter);
		applyFilter();
	}

	/**
	 * Adds passed {@link Filter} to filter chain
	 * 
	 * @param filter {@link Filter} to add
	 */
	public void addFilter(Filter<T> filter) {
		if (filter == null)
			return;
		getFilter().and(filter);
		applyFilter();
	}

	/**
	 * Removes passed {@link Filter} from filter chain
	 * 
	 * @param filter {@link Filter} to remove
	 */
	public void removeFilter(Filter<T> filter) {
		if (filter == null)
			return;
		getFilter().removeFilter(filter);
		applyFilter();
	}

	/**
	 * @return Currently active {@link Filter}
	 */
	public ExpressionFilter<T> getFilter() {
		return filter;
	}

	/**
	 * Converts {@link Filter} to {@link RowFilter}
	 * 
	 * @param filter {@link Filter} to convert
	 * @return {@link RowFilter} which mimics the function of converted {@link Filter}
	 */
	private RowFilter<TableModel, Integer> convertToRowFilter(final Filter<T> filter) {
		if (filter == null)
			return null;
		return new RowFilter<TableModel, Integer>() {
			@Override
			public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
				T rowObject = attachedTable.getDataSource().get(entry.getIdentifier());
				return filter.isAcceptable(rowObject);
			}
		};
	}

	/**
	 * Converts {@link RowFilter} to {@link Filter}
	 * 
	 * @param rowFilter {@link RowFilter} to convert
	 * @return {@link Filter} which mimics the function of converted {@link RowFilter}
	 */
	@SuppressWarnings("unused")
	private Filter<T> convertToFilter(final RowFilter<? super TableModel, ? super Integer> rowFilter) {
		if (rowFilter == null)
			return null;
		return new Filter<T>() {
			@Override
			public boolean isAcceptable(T object) {
				int modelIndex = attachedTable.getDataSource().indexOf(object);
				if (modelIndex == -1)
					return false;
				return rowFilter.include(new RowFilterEntry(modelIndex));
			}
		};
	}

	/**
	 * Sets {@link RowFilter} to the table and applies filtering
	 * 
	 * @param filter {@link RowFilter} to be set
	 */
	private void setRowFilter(RowFilter<? super TableModel, ? super Integer> filter) {
		TableRowSorter<TableModel> rowSorter = getTableRowSorter();
		rowSorter.setRowFilter(filter);
		applyFilter();
	}

	/**
	 * @return {@link RowSorter} attached to table safely cast to {@link TableRowSorter}
	 */
	@SuppressWarnings("unchecked")
	public TableRowSorter<TableModel> getTableRowSorter() {
		if (attachedTable.getTable().getRowSorter() == null)
			throw new IllegalStateException("Attached table does not have a row sorter!");
		if (!(attachedTable.getTable().getRowSorter() instanceof TableRowSorter<?>))
			throw new IllegalStateException(
					"Attached table must use TableRowSorter, but currently using: "
					+ attachedTable.getTable().getRowSorter().getClass().getSimpleName());

		return (TableRowSorter<TableModel>) attachedTable.getTable().getRowSorter();
	}

	/**
	 * Applies filtering
	 */
	public void applyFilter() {
		getTableRowSorter().sort();
	}

	/**
	 * Helper class needed when converting {@link Filter} to {@link RowFilter}
	 * 
	 * @author milos
	 * 
	 */
	private class RowFilterEntry extends Entry<TableModel, Integer> {
		private final int modelIndex;

		public RowFilterEntry(int modelIndex) {
			this.modelIndex = modelIndex;
		}

		@Override
		public Integer getIdentifier() {
			return modelIndex;
		}

		@Override
		public TableModel getModel() {
			return attachedTable.getTable().getModel();
		}

		@Override
		public Object getValue(int index) {
			return getModel().getValueAt(modelIndex, index);
		}

		@Override
		public int getValueCount() {
			return getModel().getColumnCount();
		}

	}
}
