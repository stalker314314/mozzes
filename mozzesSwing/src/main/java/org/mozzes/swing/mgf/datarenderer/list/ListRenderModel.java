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
package org.mozzes.swing.mgf.datarenderer.list;

import java.awt.Component;
import java.io.Serializable;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.mozzes.swing.mgf.datamodel.DataModel;
import org.mozzes.swing.mgf.datamodel.Field;
import org.mozzes.swing.mgf.datamodel.fields.WholeObjectField;
import org.mozzes.swing.mgf.datamodel.impl.DefaultDataModel;
import org.mozzes.swing.mgf.datarenderer.DataRenderModel;
import org.mozzes.swing.mgf.datarenderer.combobox.ComboBoxRenderModel;
import org.mozzes.swing.mgf.datasource.DataSource;
import org.mozzes.swing.mgf.datasource.SelectionListDataSource;
import org.mozzes.swing.mgf.datasource.events.selectionlist.SelectionChangedEvent;
import org.mozzes.swing.mgf.datasource.events.selectionlist.SelectionListDataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.selectionlist.SelectionListEventAdapter;
import org.mozzes.swing.mgf.datasource.impl.DefaultSelectionListDataSource;
import org.mozzes.swing.mgf.datasource.impl.SelectionMode;


/**
 * Creates a list that will display data from the {@link SelectionListDataSource source} according to the
 * {@link DataModel model}
 * 
 * @author milos
 */
public class ListRenderModel<T> implements DataRenderModel<T, SelectionListDataSource<T>> {
	private static final long serialVersionUID = 15L;

	private final DataModel<T> dataModel;
	private SelectionListDataSource<T> dataSource;
	private final CustomListModel<T> listModel;
	private final ListCellRenderer wholeObjectRenderer = new WholeObjectListRenderer();

	private JList list;
	private JScrollPane jScrollPane;

	private final InternalHandlers handlers = new InternalHandlers();

	private static <T> SelectionListDataSource<T> getDefaultDataSource(Class<T> objectType) {
		DefaultSelectionListDataSource<T> source = new DefaultSelectionListDataSource<T>(objectType);
		source.setSelectionMode(SelectionMode.SINGLE_SELECTION);
		return source;
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 */
	public ListRenderModel(Class<T> objectType) {
		this(ListRenderModel.<T> getDefaultDataSource(objectType));
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param dataSource {@link DataSource Source} that will be used to provide data for the {@link DataModel model}
	 */
	public ListRenderModel(SelectionListDataSource<T> dataSource) {
		this(dataSource, new WholeObjectField<T>(dataSource.getElementType()));
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param field {@link Field} that will be used to populate the list with data
	 */
	public ListRenderModel(Class<T> objectType, Field<T, ?> field) {
		this(objectType, new DefaultDataModel<T>().addField(field));
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param dataModel {@link DataModel Model} that will be used to construct {@link DataRenderModel render model}
	 */
	public ListRenderModel(Class<T> objectType, DataModel<T> dataModel) {
		this(ListRenderModel.<T> getDefaultDataSource(objectType), dataModel);
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param dataSource {@link DataSource Source} that will be used to provide data for the {@link DataModel model}
	 * @param field {@link Field} that will be used to populate the list with data
	 */
	public ListRenderModel(SelectionListDataSource<T> dataSource, Field<T, ?> field) {
		this(dataSource, new DefaultDataModel<T>().addField(field));
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param dataSource {@link DataSource Source} that will be used to provide data for the {@link DataModel model}
	 * @param dataModel {@link DataModel Model} that will be used to construct {@link DataRenderModel render model}
	 */
	public ListRenderModel(SelectionListDataSource<T> dataSource, DataModel<T> dataModel) {
		this(new JList(), dataSource, dataModel);
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 */
	public ListRenderModel(JList list, Class<T> objectType) {
		this(list, ListRenderModel.<T> getDefaultDataSource(objectType));
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param dataSource {@link DataSource Source} that will be used to provide data for the {@link DataModel model}
	 */
	public ListRenderModel(JList list, SelectionListDataSource<T> dataSource) {
		this(list, dataSource, new WholeObjectField<T>(dataSource.getElementType()));
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param field {@link Field} that will be used to populate the list with data
	 */
	public ListRenderModel(JList list, Class<T> objectType, Field<T, ?> field) {
		this(list, objectType, new DefaultDataModel<T>().addField(field));
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param dataModel {@link DataModel Model} that will be used to construct {@link DataRenderModel render model}
	 */
	public ListRenderModel(JList list, Class<T> objectType, DataModel<T> dataModel) {
		this(list, ListRenderModel.<T> getDefaultDataSource(objectType), dataModel);
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param dataSource {@link DataSource Source} that will be used to provide data for the {@link DataModel model}
	 * @param field {@link Field} that will be used to populate the list with data
	 */
	public ListRenderModel(JList list, SelectionListDataSource<T> dataSource, Field<T, ?> field) {
		this(list, dataSource, new DefaultDataModel<T>().addField(field));
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 */
	public ListRenderModel(Class<T> objectType,
			boolean wholeObjectSelection) {
		this(ListRenderModel.<T> getDefaultDataSource(objectType), wholeObjectSelection);
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param dataSource {@link DataSource Source} that will be used to provide data for the {@link DataModel model}
	 */
	public ListRenderModel(SelectionListDataSource<T> dataSource,
			boolean wholeObjectSelection) {
		this(dataSource, new WholeObjectField<T>(dataSource.getElementType()), wholeObjectSelection);
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param field {@link Field} that will be used to populate the list with data
	 */
	public ListRenderModel(Class<T> objectType, Field<T, ?> field,
			boolean wholeObjectSelection) {
		this(objectType, new DefaultDataModel<T>().addField(field), wholeObjectSelection);
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param dataModel {@link DataModel Model} that will be used to construct {@link DataRenderModel render model}
	 */
	public ListRenderModel(Class<T> objectType, DataModel<T> dataModel,
			boolean wholeObjectSelection) {
		this(ListRenderModel.<T> getDefaultDataSource(objectType), dataModel, wholeObjectSelection);
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param dataSource {@link DataSource Source} that will be used to provide data for the {@link DataModel model}
	 * @param field {@link Field} that will be used to populate the list with data
	 */
	public ListRenderModel(SelectionListDataSource<T> dataSource, Field<T, ?> field,
			boolean wholeObjectSelection) {
		this(dataSource, new DefaultDataModel<T>().addField(field), wholeObjectSelection);
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param dataSource {@link DataSource Source} that will be used to provide data for the {@link DataModel model}
	 * @param dataModel {@link DataModel Model} that will be used to construct {@link DataRenderModel render model}
	 */
	public ListRenderModel(SelectionListDataSource<T> dataSource, DataModel<T> dataModel,
			boolean wholeObjectSelection) {
		this(new JList(), dataSource, dataModel, wholeObjectSelection);
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 */
	public ListRenderModel(JList list, Class<T> objectType,
			boolean wholeObjectSelection) {
		this(list, ListRenderModel.<T> getDefaultDataSource(objectType), wholeObjectSelection);
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param dataSource {@link DataSource Source} that will be used to provide data for the {@link DataModel model}
	 */
	public ListRenderModel(JList list, SelectionListDataSource<T> dataSource, boolean wholeObjectSelection) {
		this(list, dataSource, new WholeObjectField<T>(dataSource.getElementType()), wholeObjectSelection);
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param field {@link Field} that will be used to populate the list with data
	 */
	public ListRenderModel(JList list, Class<T> objectType, Field<T, ?> field,
			boolean wholeObjectSelection) {
		this(list, objectType, new DefaultDataModel<T>().addField(field), wholeObjectSelection);
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param dataModel {@link DataModel Model} that will be used to construct {@link DataRenderModel render model}
	 */
	public ListRenderModel(JList list, Class<T> objectType, DataModel<T> dataModel,
			boolean wholeObjectSelection) {
		this(list, ListRenderModel.<T> getDefaultDataSource(objectType), dataModel, wholeObjectSelection);
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param dataSource {@link DataSource Source} that will be used to provide data for the {@link DataModel model}
	 * @param field {@link Field} that will be used to populate the list with data
	 */
	public ListRenderModel(JList list, SelectionListDataSource<T> dataSource, Field<T, ?> field,
			boolean wholeObjectSelection) {
		this(list, dataSource, new DefaultDataModel<T>().addField(field), wholeObjectSelection);
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param list {@link JList} to be used for presenting data
	 * @param dataSource {@link DataSource Source} that will be used to provide data for the {@link DataModel model}
	 * @param dataModel {@link DataModel Model} that will be used to construct {@link DataRenderModel render model}
	 */
	public ListRenderModel(JList list, SelectionListDataSource<T> dataSource, DataModel<T> dataModel) {
		this(list, dataSource, dataModel, true);
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param list {@link JList} to be used for presenting data
	 * @param dataSource {@link DataSource Source} that will be used to provide data for the {@link DataModel model}
	 * @param dataModel {@link DataModel Model} that will be used to construct {@link DataRenderModel render model}
	 * @param wholeObjectSelection True if the {@link ComboBoxModel#getSelectedItem() getSelectedItem} should return the
	 *            whole object selected, false if it should return the value of the specified field for selected object
	 */
	public ListRenderModel(JList list, SelectionListDataSource<T> dataSource, DataModel<T> dataModel,
			boolean wholeObjectSelection) {
		if (list == null || dataSource == null || dataModel == null)
			throw new IllegalArgumentException("None of the parameters cannot be null!");
		this.list = list;
		this.dataModel = dataModel;
		DataModel<T> listDataModel = dataModel;
		if (wholeObjectSelection) {
			listDataModel = new DefaultDataModel<T>();
			listDataModel.addField(new WholeObjectField<T>(dataSource.getElementType()));
		}
		listModel = new CustomListModel<T>(listDataModel);
		setDataSource(dataSource);
		list.setModel(listModel);
		setupListSelectionMode();
		if (wholeObjectSelection) {
			list.setCellRenderer(wholeObjectRenderer);
		}
		list.getSelectionModel().addListSelectionListener(handlers);
	}

	@Override
	public SelectionListDataSource<T> getDataSource() {
		return dataSource;
	}

	@Override
	public DataModel<T> getDataModel() {
		return dataModel;
	}

	/*
	 * (non-Javadoc) Sets the data source and attaches all the necessary listeners to it
	 * 
	 * @see org.mozzes.swing.mgf.datarenderer.DataRenderModel#setDataSource(org.mozzes.swing.mgf.datasource.DataSource)
	 */
	@Override
	public void setDataSource(SelectionListDataSource<T> source) {
		if (dataSource != null)
			dataSource.addEventListener(handlers);
		dataSource = source;
		if (dataSource == null) {
			listModel.setDataSource(null);
			return;
		}
		dataSource.addEventListener(handlers);
		setupListSelectionMode();
		listModel.setDataSource(source);
	}

	/**
	 * Sets the {@link ListSelectionModel} for the {@link JList list} based on the {@link SelectionMode} of the
	 * {@link SelectionListDataSource source}
	 */
	private void setupListSelectionMode() {
		if (dataSource == null)
			return;
		list.getSelectionModel().setSelectionMode(dataSource.getSelectionMode().getSwingSelectionMode());
	}

	/**
	 * Returns the {@link JScrollPane} containing the {@link JList list}
	 * 
	 * @see org.mozzes.swing.mgf.datarenderer.DataRenderModel#getRenderComponent()
	 */
	@Override
	public Component getRenderComponent() {
		return getScrollPane();
	}

	/**
	 * @return The {@link JScrollPane} containing the {@link JList list}
	 */
	public JScrollPane getScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane(list);
		}
		return jScrollPane;
	}

	/**
	 * @return The {@link JList} used to show data
	 */
	public JList getList() {
		return list;
	}

	/**
	 * Handles propagation of selection between the {@link SelectionListDataSource source} and the list
	 * 
	 * @author milos
	 */
	private class InternalHandlers extends SelectionListEventAdapter<T> implements ListSelectionListener,
			Serializable {
		private static final long serialVersionUID = 15L;
		private boolean toListPropagationEnabled = true;
		private boolean fromListPropagationEnabled = true;

		/*
		 * (non-Javadoc) Propagates the selection from source to listFS
		 * 
		 * @see
		 * org.mozzes.swing.mgf.datasource.events.selectionlist.SelectionListDataSourceEventListener#handleDataSourceEvent
		 * (org.mozzes.swing.mgf.datasource.SelectionListDataSource,
		 * org.mozzes.swing.mgf.datasource.events.selectionlist.SelectionListDataSourceEvent)
		 */
		@Override
		public void handleDataSourceEvent(SelectionListDataSource<T> source, SelectionListDataSourceEvent<T> event) {
			if (!toListPropagationEnabled)
				return;
			if (event instanceof SelectionChangedEvent<?>) {
				applySourceSelectionToList();
			}
		}

		/*
		 * (non-Javadoc) Propagates selection from list to source
		 * 
		 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
		 */
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting() || !fromListPropagationEnabled)
				return;
			if (!listModel.isSelectionPropagationEnabled()) {
				// This is executed when JList tries to be smart(when it tries to modify the selection based on event
				// such as RowsAdded or RowsRemoved), we are preventing that since the source has already modified the
				// selection, so we are only reapplying it to table
				applySourceSelectionToList();
				return;
			}
			int[] indices = list.getSelectedIndices();
			toListPropagationEnabled = false;
			dataSource.setSelectedIndices(indices);
			toListPropagationEnabled = true;
		}

		private void applySourceSelectionToList() {
			fromListPropagationEnabled = false;
			list.clearSelection();
			List<Integer> selectedIndices = dataSource.getSelectedIndices();
			for (Integer index : selectedIndices) {
				list.addSelectionInterval(index, index);
			}
			fromListPropagationEnabled = true;
		}
	}

	private class WholeObjectListRenderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		@SuppressWarnings("unchecked")
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			Component listCellRendererComponent = super.getListCellRendererComponent(list, value, index,
					isSelected, cellHasFocus);
			T object = (T) value;
			Object fieldValue = getDataModel().getField(0).getValue(object);
			setText(fieldValue == null ? "" : fieldValue.toString());
			return listCellRendererComponent;
		}
	}
}