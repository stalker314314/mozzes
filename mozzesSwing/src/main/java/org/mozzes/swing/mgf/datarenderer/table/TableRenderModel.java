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
package org.mozzes.swing.mgf.datarenderer.table;

import java.awt.Component;
import java.io.Serializable;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.mozzes.swing.mgf.component.table.editing.CellEditingFacility;
import org.mozzes.swing.mgf.component.table.helpers.TableCellFocusManager;
import org.mozzes.swing.mgf.component.table.rendering.CellRenderingFacility;
import org.mozzes.swing.mgf.datamodel.DataModel;
import org.mozzes.swing.mgf.datamodel.Field;
import org.mozzes.swing.mgf.datamodel.events.DataModelEvent;
import org.mozzes.swing.mgf.datamodel.events.DataModelEventListener;
import org.mozzes.swing.mgf.datamodel.events.FieldAddedEvent;
import org.mozzes.swing.mgf.datamodel.events.FieldMovedEvent;
import org.mozzes.swing.mgf.datamodel.events.FieldRemovedEvent;
import org.mozzes.swing.mgf.datarenderer.DataRenderModel;
import org.mozzes.swing.mgf.datarenderer.table.filter.FilteringFacility;
import org.mozzes.swing.mgf.datasource.DataSource;
import org.mozzes.swing.mgf.datasource.ListDataSource;
import org.mozzes.swing.mgf.datasource.SelectionListDataSource;
import org.mozzes.swing.mgf.datasource.events.selectionlist.SelectionChangedEvent;
import org.mozzes.swing.mgf.datasource.events.selectionlist.SelectionListDataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.selectionlist.SelectionListEventAdapter;
import org.mozzes.swing.mgf.datasource.impl.DefaultSelectionListDataSource;
import org.mozzes.swing.mgf.datasource.impl.SelectionMode;


/**
 * Creates a table that will display data from the {@link SelectionListDataSource source} according to the
 * {@link DataModel model}
 * 
 * @author milos
 * 
 * @param <T> Type of the bean contained in one row of the table
 */
public class TableRenderModel<T> implements DataRenderModel<T, SelectionListDataSource<T>> {
	private static final long serialVersionUID = 15L;

	private SelectionListDataSource<T> dataSource;
	private final CustomTableModel<T> tableModel;
	private final JTable table;
	private JScrollPane jScrollPane;
	private final TableRowSorter<CustomTableModel<T>> rowSorter =
			new TableRowSorter<CustomTableModel<T>>();
	private final InternalHandlers handlers = new InternalHandlers();
	private boolean showRowNumberColumn;
	private final CellRenderingFacility cellRenderingFacility;
	private final CellEditingFacility cellEditingFacility;
	private final FilteringFacility<T> filteringFacility;

	private int rowNumberColumnWidth = 40;

	/**
	 * Constructs the {@link TableRenderModel}
	 * 
	 * @param objectType Class of the object that will be represented by one row of the table
	 * @param dataModel {@link DataModel Model} that will be used define the presentation of data
	 */
	public TableRenderModel(Class<T> objectType, DataModel<T> dataModel) {
		this(new JTable(), objectType, dataModel, true);
	}

	/**
	 * Constructs the {@link TableRenderModel}
	 * 
	 * @param objectType Class of the object that will be represented by one row of the table
	 * @param dataModel {@link DataModel Model} that will be used define the presentation of data
	 * @param showRowNumberColumn Pass <i>true</i> if you want row number column to be shown.
	 */
	public TableRenderModel(Class<T> objectType, DataModel<T> dataModel, boolean showRowNumberColumn) {
		this(new JTable(), objectType, dataModel, showRowNumberColumn);
	}

	/**
	 * Constructs the {@link TableRenderModel}
	 * 
	 * @param dataSource {@link DataSource Source} that will be used to provide data for the {@link DataModel model}
	 * @param dataModel {@link DataModel Model} that will be used define the presentation of data
	 */
	public TableRenderModel(SelectionListDataSource<T> dataSource, DataModel<T> dataModel) {
		this(new JTable(), dataSource, dataModel, true);
	}

	/**
	 * Constructs the {@link TableRenderModel}
	 * 
	 * @param dataSource {@link DataSource Source} that will be used to provide data for the {@link DataModel model}
	 * @param dataModel {@link DataModel Model} that will be used define the presentation of data
	 * @param showRowNumberColumn Pass <i>true</i> if you want row number column to be shown.
	 */
	public TableRenderModel(SelectionListDataSource<T> dataSource, DataModel<T> dataModel, boolean showRowNumberColumn) {
		this(new JTable(), dataSource, dataModel, showRowNumberColumn);
	}

	/**
	 * Constructs the {@link TableRenderModel}
	 * 
	 * @param table {@link JTable} to be used for presenting data
	 * @param objectType Class of the object that will be represented by one row of the table
	 * @param dataModel {@link DataModel Model} that will be used define the presentation of data
	 */
	public TableRenderModel(JTable table, Class<T> objectType, DataModel<T> dataModel) {
		this(table, new DefaultSelectionListDataSource<T>(objectType), dataModel, true);
	}

	/**
	 * Constructs the {@link TableRenderModel}
	 * 
	 * @param table {@link JTable} to be used for presenting data
	 * @param objectType Class of the object that will be represented by one row of the table
	 * @param dataModel {@link DataModel Model} that will be used define the presentation of data
	 * @param showRowNumberColumn Pass <i>true</i> if you want row number column to be shown.
	 */
	public TableRenderModel(JTable table, Class<T> objectType, DataModel<T> dataModel, boolean showRowNumberColumn) {
		this(table, new DefaultSelectionListDataSource<T>(objectType), dataModel, showRowNumberColumn);
	}

	/**
	 * Constructs the {@link TableRenderModel}
	 * 
	 * @param table {@link JTable} to be used for presenting data
	 * @param dataSource {@link DataSource Source} that will be used to provide data for the {@link DataModel model}
	 * @param dataModel {@link DataModel Model} that will be used define the presentation of data
	 */
	public TableRenderModel(JTable table, SelectionListDataSource<T> dataSource, DataModel<T> dataModel) {
		this(table, dataSource, dataModel, true);
	}

	/**
	 * Constructs the {@link TableRenderModel}
	 * 
	 * @param table {@link JTable} to be used for presenting data
	 * @param dataSource {@link DataSource Source} that will be used to provide data for the {@link DataModel model}
	 * @param dataModel {@link DataModel Model} that will be used define the presentation of data
	 * @param showRowNumberColumn Pass <i>true</i> if you want row number column to be shown.
	 */
	public TableRenderModel(JTable table, SelectionListDataSource<T> dataSource, DataModel<T> dataModel,
			boolean showRowNumberColumn) {
		if (table == null)
			throw new IllegalArgumentException("Table cannot be null!");
		this.table = table;
		this.cellRenderingFacility = new CellRenderingFacility(table);
		this.cellEditingFacility = new CellEditingFacility(table);
		// TableCellFocusManager tableCellFocusManager =
		new TableCellFocusManager(table);
		tableModel = new CustomTableModel<T>(dataModel);
		dataModel.addEventListener(handlers);
		setDataSource(dataSource);
		setupTable();
		this.showRowNumberColumn = showRowNumberColumn;
		this.filteringFacility = new FilteringFacility<T>(this);
	}

	@Override
	public SelectionListDataSource<T> getDataSource() {
		return dataSource;
	}

	@Override
	public DataModel<T> getDataModel() {
		return tableModel.getDataModel();
	}

	@Override
	public void setDataSource(SelectionListDataSource<T> source) {
		if (dataSource != null)
			dataSource.removeEventListener(handlers);
		dataSource = source;
		if (dataSource == null) {
			tableModel.setDataSource(null);
			return;
		}
		dataSource.addEventListener(handlers);
		setupTableSelectionMode();
		tableModel.setDataSource(source);
	}

	/**
	 * Configures the {@link RowSorter rowSorter} according to the {@link Field#isSortable() sortable} property of
	 * {@link Field fields}
	 */
	private void setupRowSorter() {
		DataModel<T> dataModel = getDataModel();
		rowSorter.setModel(tableModel);
		for (int i = 0; i < tableModel.getColumnCount(); i++) {
			rowSorter.setSortable(i, dataModel.getField(i).isSortable());
			rowSorter.setComparator(i, dataModel.getField(i).getComparator());
		}
		table.setRowSorter(rowSorter);
	}

	/**
	 * Sets up the table to use the appropriate {@link TableModel model}, {@link RowSorter sorter},
	 * {@link ListSelectionModel selection mode} and all the required handlers
	 */
	private void setupTable() {
		table.getTableHeader().setReorderingAllowed(false);
		table.setModel(tableModel);
		setupRowSorter();
		table.getSelectionModel().addListSelectionListener(handlers);
		table.setCellSelectionEnabled(false);
		table.setRowSelectionAllowed(true);
	}

	/**
	 * Sets the {@link ListSelectionModel} for the table based on the {@link SelectionMode} of the
	 * {@link SelectionListDataSource source}
	 */
	private void setupTableSelectionMode() {
		if (dataSource == null)
			return;
		table.getSelectionModel().setSelectionMode(dataSource.getSelectionMode().getSwingSelectionMode());
	}

	/**
	 * Returns the {@link JScrollPane} containing the table
	 * 
	 * @see org.mozzes.swing.mgf.datarenderer.DataRenderModel#getRenderComponent()
	 */
	@Override
	public Component getRenderComponent() {
		return getScrollPane();
	}

	/**
	 * @return The {@link JScrollPane} containing the table
	 */
	public JScrollPane getScrollPane() {
		if (jScrollPane == null) {
			if (!showRowNumberColumn)
				jScrollPane = new JScrollPane(table);
			else {
				jScrollPane = RowNumberTable.createScrollPane(table);
				setRowNumberColumnWidth(rowNumberColumnWidth);
			}
		}
		return jScrollPane;
	}

	/**
	 * @return The {@link JTable} used to show data
	 */
	public JTable getTable() {
		return table;
	}

	/**
	 * @return An object which provides methods for setting up cell renderers and cell decorators
	 */
	public CellRenderingFacility getCellRenderingFacility() {
		return cellRenderingFacility;
	}

	/**
	 * @return An object which provides methods for setting up cell editors
	 */
	public CellEditingFacility getCellEditingFacility() {
		return cellEditingFacility;
	}

	public FilteringFacility<T> getFilteringFacility() {
		return filteringFacility;
	}

	/**
	 * @return <b>true</b> if the Row number column is visible, <b>false</b> otherwise
	 */
	public boolean isRowNumberColumnVisible() {
		return getScrollPaneWithRowHeader() != null;
	}

	/**
	 * @return Width of the row number column (0 if there is no row number column)
	 */
	public int getRowNumberColumnWidth() {
		if (getScrollPaneWithRowHeader() == null)
			return rowNumberColumnWidth;
		return getScrollPaneWithRowHeader().getRowHeaderTable().getWidth();
	}

	/**
	 * Sets the width of the row number column if it exists, does nothing otherwise
	 * 
	 * @param width Width to set(in pixels)
	 */
	public void setRowNumberColumnWidth(int width) {
		rowNumberColumnWidth = width;
		if (getScrollPaneWithRowHeader() == null)
			return;
		((RowNumberTable) getScrollPaneWithRowHeader().getRowHeaderTable()).setWidth(width);
	}

	/**
	 * @return Current scroll pane cast to ScrollPaneWithRowHeader (if unable to cast, null is returned)
	 */
	private ScrollPaneWithRowHeader getScrollPaneWithRowHeader() {
		if (!(jScrollPane instanceof ScrollPaneWithRowHeader))
			return null;
		return (ScrollPaneWithRowHeader) jScrollPane;
	}

	/**
	 * This is the handler that takes care of the propagation of {@link SelectionListDataSourceEvent selection events}
	 * between the source and the table
	 * 
	 * @author milos
	 */
	private class InternalHandlers extends SelectionListEventAdapter<T> implements ListSelectionListener,
			DataModelEventListener<T>, Serializable {
		private static final long serialVersionUID = 15L;
		private boolean toTablePropagationEnabled = true;
		private boolean fromTablePropagationEnabled = true;

		/*
		 * (non-Javadoc) Propagates the selection from source to table
		 * 
		 * @see
		 * org.mozzes.swing.mgf.datasource.events.selectionlist.SelectionListDataSourceEventListener#handleDataSourceEvent
		 * (org.mozzes.swing.mgf.datasource.SelectionListDataSource,
		 * org.mozzes.swing.mgf.datasource.events.selectionlist.SelectionListDataSourceEvent)
		 */
		@Override
		public void handleDataSourceEvent(SelectionListDataSource<T> source, SelectionListDataSourceEvent<T> event) {
			if (!toTablePropagationEnabled)
				return;
			if (event instanceof SelectionChangedEvent<?>) {
				applySourceSelectionToTable();
			}
		}

		/**
		 * Does the actual setting of the selection on the able
		 */
		private void applySourceSelectionToTable() {
			fromTablePropagationEnabled = false;
			table.clearSelection();
			List<Integer> selectedIndices = dataSource.getSelectedIndices();
			for (Integer index : selectedIndices) {
				index = table.convertRowIndexToView(index);
				table.addRowSelectionInterval(index, index);
			}
			fromTablePropagationEnabled = true;
		}

		/**
		 * Propagates {@link DataModelEvent events} to {@link ListDataSource}
		 * 
		 * @see org.mozzes.swing.mgf.datamodel.events.DataModelEventListener#handleDataModelEvent(org.mozzes.swing.mgf.datamodel.DataModel,
		 *      org.mozzes.swing.mgf.datamodel.events.DataModelEvent)
		 */
		@Override
		public void handleDataModelEvent(DataModel<T> model, DataModelEvent<T> event) {
			if (!(event instanceof FieldAddedEvent<?> ||
					event instanceof FieldRemovedEvent<?> || event instanceof FieldMovedEvent<?>))
				return;
			setupRowSorter();
		}

		/*
		 * (non-Javadoc) Propagates the selection from table to source
		 * 
		 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
		 */
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting() || !fromTablePropagationEnabled || !table.getRowSelectionAllowed())
				return;
			if (!tableModel.isSelectionPropagationEnabled()) {
				// This is executed when JTable tries to be smart(when it tries to modify the selection based on event
				// such as RowsAdded or RowsRemoved), we are preventing that since the source has already modified the
				// selection, so we are only reapplying it to table
				// applySourceSelectionToTable();
				table.clearSelection();
				return;
			}
			int[] indices = convertIndicesToModel(table.getSelectedRows());
			toTablePropagationEnabled = false;
			dataSource.setSelectedIndices(indices);
			toTablePropagationEnabled = true;
		}

		/**
		 * Converts an array of indices expressed in view space to an array of indices in model space
		 * 
		 * @param indices View space indices list
		 * @return An array representing the indices in model space
		 */
		private int[] convertIndicesToModel(int... indices) {
			for (int i = 0; i < indices.length; i++) {
				indices[i] = table.convertRowIndexToModel(indices[i]);
			}
			return indices;
		}
	}
}