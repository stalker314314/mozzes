package org.mozzes.swing.mgf.datarenderer.table;

import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.mozzes.swing.mgf.datamodel.DataModel;
import org.mozzes.swing.mgf.datamodel.Field;
import org.mozzes.swing.mgf.datamodel.events.DataModelEvent;
import org.mozzes.swing.mgf.datamodel.events.DataModelEventListener;
import org.mozzes.swing.mgf.datamodel.events.FieldAddedEvent;
import org.mozzes.swing.mgf.datamodel.events.FieldMovedEvent;
import org.mozzes.swing.mgf.datamodel.events.FieldRemovedEvent;
import org.mozzes.swing.mgf.datamodel.events.FieldValueUpdatedEvent;
import org.mozzes.swing.mgf.datasource.BeanDataSource;
import org.mozzes.swing.mgf.datasource.DataSource;
import org.mozzes.swing.mgf.datasource.ListDataSource;
import org.mozzes.swing.mgf.datasource.events.DataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.SourceChangedEvent;
import org.mozzes.swing.mgf.datasource.events.bean.BeanDataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.bean.DataUpdatedEvent;
import org.mozzes.swing.mgf.datasource.events.list.ListDataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.list.ListEventAdapter;
import org.mozzes.swing.mgf.datasource.events.list.ObjectReplacedEvent;
import org.mozzes.swing.mgf.datasource.events.list.ObjectsAddedEvent;
import org.mozzes.swing.mgf.datasource.events.list.ObjectsRemovedEvent;
import org.mozzes.swing.mgf.datasource.events.list.ObjectsUpdatedEvent;


/**
 * Custom {@link TableModel} which is bound to a data source and reacts to its events properly
 * 
 * @author milos
 * 
 * @param <T> Type of the bean contained in one row of the table using this model
 */
public class CustomTableModel<T> extends AbstractTableModel {
	private static final long serialVersionUID = 15L;

	private final DataModel<T> dataModel;
	private ListDataSource<T> dataSource;

	private final InternalHandlers handlers = new InternalHandlers();

	private boolean selectionPropagationEnabled = true;

	/**
	 * @param dataModel {@link DataModel} to be used to represents {@link TableModel model} columns
	 */
	public CustomTableModel(DataModel<T> dataModel) {
		this.dataModel = dataModel;
		setupDataModel();
	}

	/**
	 * @param dataModel {@link DataModel} to be used to represents {@link TableModel model} columns
	 * @param dataSource {@link ListDataSource Data source} to be used to provide rows
	 */
	public CustomTableModel(DataModel<T> dataModel, ListDataSource<T> dataSource) {
		this.dataModel = dataModel;
		setupDataModel();
		setDataSource(dataSource);
	}

	/**
	 * @return {@link DataModel} in use by this {@link CustomTableModel table model} instance
	 */
	public DataModel<T> getDataModel() {
		return dataModel;
	}

	/**
	 * @return {@link ListDataSource Data source} in use by this {@link CustomTableModel table model} instance
	 */
	public ListDataSource<T> getDataSource() {
		return dataSource;
	}

	/**
	 * Attaches all necessary listeners to the {@link DataModel}<br>
	 * <b>[NOTICE] this method should only be called once during the construction of {@link CustomTableModel}</b>
	 */
	private void setupDataModel() {
		if (dataModel == null)
			throw new IllegalArgumentException("DataModel cannot be null!");
		dataModel.removeEventListener(handlers);
		dataModel.addEventListener(handlers);
	}

	/**
	 * Sets the specified {@link ListDataSource source} to be used for feeding the data to the {@link CustomTableModel
	 * table model} and attaches all necessary listeners to it
	 * 
	 * @param source {@link ListDataSource source} to be used for feeding the data to the {@link CustomTableModel table
	 *            model}
	 */
	public void setDataSource(ListDataSource<T> source) {
		if (dataSource != null)
			dataSource.removeEventListener(handlers);
		dataSource = source;
		if (dataSource == null)
			return;
		dataSource.addEventListener(handlers);
		fireTableDataChanged();
	}

	/*
	 * (non-Javadoc) Uses {@link Field#getHeader() field header} if not empty, else, lets {@link AbstractTableModel}
	 * decide on the name of the column
	 * 
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) {
		if (column >= getColumnCount())
			throw new IllegalArgumentException(String.format(
					"Column with index %d does not exist. Number of columns: %d.",
					column, getColumnCount()));
		if (dataModel.getField(column).getHeader() == null)
			return super.getColumnName(column);
		else
			return dataModel.getField(column).getHeader();
	}

	/*
	 * (non-Javadoc) Returns column count based on the column count in the {@link DataModel model}
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return dataModel.getFieldsNumber();
	}

	/*
	 * (non-Javadoc) Returns rows count based on the count of objects in the {@link ListDataSource data source}
	 * 
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return dataSource == null ? 0 : dataSource.getSize();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (dataSource == null) {
			throw new NullPointerException("DataSource cannot be null!");
		}
		if (rowIndex >= dataSource.getSize())
			throw new IndexOutOfBoundsException(String.format("Row index: %d; Number of rows: %d.",
					rowIndex, dataSource.getSize()));
		if (columnIndex >= dataModel.getFieldsNumber())
			throw new IndexOutOfBoundsException(String.format("Column index: %d; Number of columns: %d.",
					columnIndex, dataModel.getFieldsNumber()));
		return dataModel.getFieldValue(columnIndex, dataSource.get(rowIndex));
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		T object = dataSource.get(rowIndex);
		Field<T, Object> field = dataModel.getField(columnIndex);
		if (!isCellEditable(rowIndex, columnIndex))
			throw new UnsupportedOperationException("Cell is not editable!");

		handlers.propagateDataModelEvents = false;
		field.setValue(object, aValue);
		dataSource.fireObjectsUpdatedEvent(object);
		handlers.propagateDataModelEvents = true;
		fireTableCellUpdated(rowIndex, columnIndex);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return dataModel.getField(columnIndex).getFieldType();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		Field<T, Object> field = dataModel.getField(columnIndex);
		T object = dataSource.get(rowIndex);
		return !field.isReadOnly() && field.isEditableFor(object);
	}

	/**
	 * @return True if the propagation should be done, false otherwise
	 */
	boolean isSelectionPropagationEnabled() {
		return selectionPropagationEnabled;
	}

	/**
	 * Coordinates propagation of events between the {@link ListDataSource data source}, {@link DataModel data model}
	 * and {@link TableModel table model}
	 * 
	 * @author milos
	 */
	private class InternalHandlers extends ListEventAdapter<T> implements DataModelEventListener<T> {
		private boolean propagateDataModelEvents = true;

		/**
		 * Propagates {@link ListDataSourceEvent events} to {@link TableModel}
		 * 
		 * @see org.mozzes.swing.mgf.datasource.events.list.ListDataSourceEventListener#handleDataSourceEvent(org.mozzes.swing.mgf.datasource.ListDataSource,
		 *      org.mozzes.swing.mgf.datasource.events.list.ListDataSourceEvent)
		 */
		@Override
		public void handleDataSourceEvent(ListDataSource<T> source, ListDataSourceEvent<T> event) {
			selectionPropagationEnabled = false;
			if (event instanceof ObjectsAddedEvent<?>)
				rowsAdded((ObjectsAddedEvent<T>) event);
			if (event instanceof ObjectsRemovedEvent<?>)
				rowsDeleted((ObjectsRemovedEvent<T>) event);
			if (event instanceof ObjectsUpdatedEvent<?>)
				rowsUpdated((ObjectsUpdatedEvent<T>) event);
			if (event instanceof ObjectReplacedEvent<?>)
				rowUpdated((ObjectReplacedEvent<T>) event);
			selectionPropagationEnabled = true;
		}

		/**
		 * Propagates {@link DataSourceEvent events} to {@link TableModel}
		 * 
		 * @see org.mozzes.swing.mgf.datasource.events.DataSourceEventListener#handleDataSourceEvent(org.mozzes.swing.mgf.datasource.DataSource,
		 *      org.mozzes.swing.mgf.datasource.events.DataSourceEvent)
		 */
		@Override
		public void handleDataSourceEvent(BeanDataSource<List<T>> source, BeanDataSourceEvent<List<T>> event) {
			selectionPropagationEnabled = false;
			if (event instanceof DataUpdatedEvent<?>)
				fireTableDataChanged();
			selectionPropagationEnabled = true;
		}

		/**
		 * Propagates {@link DataSourceEvent events} to {@link TableModel}
		 * 
		 * @see org.mozzes.swing.mgf.datasource.events.DataSourceEventListener#handleDataSourceEvent(org.mozzes.swing.mgf.datasource.DataSource,
		 *      org.mozzes.swing.mgf.datasource.events.DataSourceEvent)
		 */
		@Override
		public void handleDataSourceEvent(DataSource<List<T>> source, DataSourceEvent<List<T>> event) {
			selectionPropagationEnabled = false;
			if (event instanceof SourceChangedEvent<?>)
				fireTableDataChanged();
			if (event instanceof DataUpdatedEvent<?>)
				fireTableDataChanged();
			selectionPropagationEnabled = true;
		}

		/**
		 * Propagates {@link DataModelEvent events} to {@link ListDataSource}
		 * 
		 * @see org.mozzes.swing.mgf.datamodel.events.DataModelEventListener#handleDataModelEvent(org.mozzes.swing.mgf.datamodel.DataModel,
		 *      org.mozzes.swing.mgf.datamodel.events.DataModelEvent)
		 */
		@Override
		public void handleDataModelEvent(DataModel<T> model, DataModelEvent<T> event) {
			if (!propagateDataModelEvents)
				return;

			if (event instanceof FieldValueUpdatedEvent<?>) {
				FieldValueUpdatedEvent<T> fieldValueUpdated = (FieldValueUpdatedEvent<T>) event;
				dataSource.fireObjectsUpdatedEvent(fieldValueUpdated.getForObject());
			}
			if (event instanceof FieldRemovedEvent<?> || event instanceof FieldMovedEvent<?>
					|| event instanceof FieldAddedEvent<?>) {
				fireTableStructureChanged();
			}
		}

		private void rowUpdated(ObjectReplacedEvent<T> event) {
			fireTableRowsUpdated(event.getIndex(), event.getIndex());
		}

		private void rowsUpdated(@SuppressWarnings("unused") ObjectsUpdatedEvent<T> event) {
			// fireTableRowsUpdated(event.getIndex(), event.getIndex() + event.getObjects().size() - 1);
			fireTableRowsUpdated(0, getLastIndex());
		}

		private void rowsDeleted(@SuppressWarnings("unused") ObjectsRemovedEvent<T> event) {
			// fireTableRowsDeleted(event.getIndex(), event.getIndex() + event.getObjects().size() - 1);
			fireTableDataChanged();
		}

		private void rowsAdded(ObjectsAddedEvent<T> event) {
			fireTableRowsInserted(event.getIndex(), event.getIndex() + event.getObjects().size() - 1);
			// fireTableDataChanged();
		}

		private int getLastIndex() {
			int to = dataSource.getSize() - 1;
			to = to >= 0 ? to : 0;
			return to;
		}
	}
}
