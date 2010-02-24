package org.mozzes.swing.mgf.datarenderer.combobox;

import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import org.mozzes.swing.mgf.datamodel.DataModel;
import org.mozzes.swing.mgf.datamodel.events.DataModelEvent;
import org.mozzes.swing.mgf.datamodel.events.DataModelEventListener;
import org.mozzes.swing.mgf.datamodel.events.FieldAddedEvent;
import org.mozzes.swing.mgf.datamodel.events.FieldMovedEvent;
import org.mozzes.swing.mgf.datamodel.events.FieldRemovedEvent;
import org.mozzes.swing.mgf.datamodel.events.FieldValueUpdatedEvent;
import org.mozzes.swing.mgf.datasource.BeanDataSource;
import org.mozzes.swing.mgf.datasource.DataSource;
import org.mozzes.swing.mgf.datasource.ListDataSource;
import org.mozzes.swing.mgf.datasource.SelectionListDataSource;
import org.mozzes.swing.mgf.datasource.events.DataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.SourceChangedEvent;
import org.mozzes.swing.mgf.datasource.events.bean.BeanDataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.bean.DataUpdatedEvent;
import org.mozzes.swing.mgf.datasource.events.list.ListDataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.list.ObjectReplacedEvent;
import org.mozzes.swing.mgf.datasource.events.list.ObjectsAddedEvent;
import org.mozzes.swing.mgf.datasource.events.list.ObjectsRemovedEvent;
import org.mozzes.swing.mgf.datasource.events.list.ObjectsUpdatedEvent;
import org.mozzes.swing.mgf.datasource.events.selectionlist.SelectionChangedEvent;
import org.mozzes.swing.mgf.datasource.events.selectionlist.SelectionListDataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.selectionlist.SelectionListEventAdapter;
import org.mozzes.swing.mgf.datasource.impl.SelectionMode;


/**
 * Custom {@link ComboBoxModel} which is bound to a data source and reacts to its events properly
 * 
 * @author milos
 * 
 * @param <T> Type of the bean contained in one row of the ComboBox using this model
 */
class CustomComboBoxModel<T> extends AbstractListModel implements ComboBoxModel {
	private static final long serialVersionUID = 15L;

	private SelectionListDataSource<T> dataSource;
	private final DataModel<T> dataModel;

	private final InternalHandlers handlers = new InternalHandlers();

	private boolean comboIsTryingToBeSmart;

	/**
	 * @param model {@link DataModel} to be used to present the data
	 */
	public CustomComboBoxModel(DataModel<T> model) {
		this(model, null);
	}

	/**
	 * @param model {@link DataModel} to be used to present the data
	 * @param source {@link ListDataSource Data source} to be used to provide rows
	 */
	public CustomComboBoxModel(DataModel<T> model, SelectionListDataSource<T> source) {
		this.dataModel = model;
		this.setupDataModel();
		if (source != null)
			this.setDataSource(source);
	}

	@Override
	public Object getElementAt(int index) {
		checkModelAndSource();
		T element = dataSource.get(index);
		return dataModel.getFieldValue(0, element);
	}

	/*
	 * (non-Javadoc) Returns rows count based on the count of objects in the {@link ListDataSource data source}
	 * 
	 * @see javax.swing.ListModel#getSize()
	 */
	@Override
	public int getSize() {
		return dataSource == null ? 0 : dataSource.getSize();
	}

	/**
	 * @return {@link SelectionListDataSource Data source} in use by this instance
	 */
	public SelectionListDataSource<T> getDataSource() {
		return dataSource;
	}

	/**
	 * @return {@link DataModel} in use by this instance
	 */
	public DataModel<T> getDataModel() {
		return dataModel;
	}

	/**
	 * Attaches all necessary listeners to the {@link DataModel}<br>
	 * <b>[NOTICE] this method should only be called once during the construction of {@link CustomComboBoxModel}</b>
	 */
	private void setupDataModel() {
		if (dataModel == null)
			throw new IllegalArgumentException("Model cannot be null!");
		if (dataModel.getFieldsNumber() != 1)
			throw new IllegalArgumentException("Model has to have exactly one field!");
		if (dataModel != null)
			dataModel.removeEventListener(handlers);
		dataModel.addEventListener(handlers);
	}

	/**
	 * Sets the specified {@link SelectionListDataSource source} to be used for feeding the data to the
	 * {@link CustomComboBoxModel ComboBox model} and attaches all necessary listeners to it
	 * 
	 * @param source {@link ListDataSource source} to be used for feeding the data to the {@link CustomComboBoxModel
	 *            ComboBox model}
	 */
	public void setDataSource(SelectionListDataSource<T> source) {
		if (source != null && !source.getSelectionMode().equals(SelectionMode.SINGLE_SELECTION))
			throw new IllegalArgumentException("DataSource selection mode must be SelectionMode.SINGLE_SELECTION!");
		if (dataSource != null)
			dataSource.removeEventListener(handlers);
		dataSource = source;
		if (dataSource == null)
			return;
		dataSource.addEventListener(handlers);
	}

	@Override
	public Object getSelectedItem() {
		if (comboIsTryingToBeSmart)
			return null;
		T selected = dataSource.getSelectedValue();
		return dataModel.getFieldValue(0, selected);
	}

	@Override
	public void setSelectedItem(Object anItem) {
		checkModelAndSource();
		if (comboIsTryingToBeSmart)
			return;

		if (anItem == null) {
			handlers.toComboBoxPropagationEnabled = false;
			dataSource.clearSelection();
			fireContentsChanged(this, 0, dataSource.getSize());
			handlers.toComboBoxPropagationEnabled = true;
			return;
		}

		if (dataSource.getSize() == 0)
			return;

		Class<?> type = dataModel.getField(0).getFieldType();
		if (!type.isAssignableFrom(anItem.getClass())) {
			throw new IllegalArgumentException("Item must be an instance of " + type.getSimpleName());
		}
		int index = -1;
		for (int i = 0; i < dataSource.getSize(); i++) {
			T element = dataSource.get(i);
			if (!anItem.equals(dataModel.getFieldValue(0, element)))
				continue;
			index = i;
			break;
		}
		if (index == -1)
			throw new IllegalArgumentException("Item you are trying to select is not contained by the ComboBox!");
		handlers.toComboBoxPropagationEnabled = false;
		dataSource.setSelectedIndices(index);
		fireContentsChanged(this, index, index);
		handlers.toComboBoxPropagationEnabled = true;
	}

	/**
	 * Performs some basic checks on {@link DataModel} and {@link SelectionListDataSource DataSource}
	 */
	private void checkModelAndSource() {
		if (dataSource == null) {
			throw new IllegalStateException("DataSource cannot be null!");
		}
		if (dataModel.getFieldsNumber() != 1)
			throw new IllegalStateException("Model has to have exactly one field!");
	}

	/**
	 * Coordinates propagation of events between the {@link SelectionListDataSource data source}, {@link DataModel data
	 * model} and {@link ComboBoxModel ComboBox model}
	 * 
	 * @author milos
	 */
	private class InternalHandlers extends SelectionListEventAdapter<T> implements DataModelEventListener<T> {
		private boolean toComboBoxPropagationEnabled = true;

		/**
		 * Propagates {@link SelectionListDataSourceEvent events} to {@link ComboBoxModel}
		 * 
		 * @see org.mozzes.swing.mgf.datasource.events.selectionlist.SelectionListDataSourceEventListener#handleDataSourceEvent
		 *      (org.mozzes.swing.mgf.datasource.SelectionListDataSource,
		 *      org.mozzes.swing.mgf.datasource.events.selectionlist.SelectionListDataSourceEvent)
		 */
		@Override
		public void handleDataSourceEvent(SelectionListDataSource<T> source, SelectionListDataSourceEvent<T> event) {
			if (!toComboBoxPropagationEnabled)
				return;
			if (event instanceof SelectionChangedEvent<?>) {
				fireContentsChanged(this, 0, dataSource.getSize());
			}
		}

		/**
		 * Propagates {@link ListDataSourceEvent events} to {@link ComboBoxModel}
		 * 
		 * @see org.mozzes.swing.mgf.datasource.events.list.ListDataSourceEventListener#handleDataSourceEvent(org.mozzes.swing.mgf.datasource.ListDataSource,
		 *      org.mozzes.swing.mgf.datasource.events.list.ListDataSourceEvent)
		 */
		@Override
		public void handleDataSourceEvent(ListDataSource<T> source, ListDataSourceEvent<T> event) {
			comboIsTryingToBeSmart = true;
			if (event instanceof ObjectsAddedEvent<?>)
				rowsAdded((ObjectsAddedEvent<T>) event);
			if (event instanceof ObjectsRemovedEvent<?>)
				rowsDeleted((ObjectsRemovedEvent<T>) event);
			if (event instanceof ObjectsUpdatedEvent<?>)
				rowsUpdated((ObjectsUpdatedEvent<T>) event);
			if (event instanceof ObjectReplacedEvent<?>)
				rowUpdated((ObjectReplacedEvent<T>) event);
			comboIsTryingToBeSmart = false;
		}

		/**
		 * Propagates {@link DataSourceEvent events} to {@link ComboBoxModel}
		 * 
		 * @see org.mozzes.swing.mgf.datasource.events.DataSourceEventListener#handleDataSourceEvent(org.mozzes.swing.mgf.datasource.DataSource,
		 *      org.mozzes.swing.mgf.datasource.events.DataSourceEvent)
		 */
		@Override
		public void handleDataSourceEvent(BeanDataSource<List<T>> source, BeanDataSourceEvent<List<T>> event) {
			comboIsTryingToBeSmart = true;
			if (event instanceof DataUpdatedEvent<?>)
				fireAllRowsUpdated();
			comboIsTryingToBeSmart = false;
		}

		/**
		 * Propagates {@link DataSourceEvent events} to {@link ComboBoxModel}
		 * 
		 * @see org.mozzes.swing.mgf.datasource.events.DataSourceEventListener#handleDataSourceEvent(org.mozzes.swing.mgf.datasource.DataSource,
		 *      org.mozzes.swing.mgf.datasource.events.DataSourceEvent)
		 */
		@Override
		public void handleDataSourceEvent(DataSource<List<T>> source, DataSourceEvent<List<T>> event) {
			comboIsTryingToBeSmart = true;
			if (event instanceof SourceChangedEvent<?>)
				fireAllRowsUpdated();
			if (event instanceof DataUpdatedEvent<?>)
				fireAllRowsUpdated();
			comboIsTryingToBeSmart = false;
			// Select first if nothing is selected! -- Does not work with
			// if ((event instanceof SourceChangedEvent<?>) || (event instanceof DataUpdatedEvent<?>))
			// if (dataSource.getSelectedIndex() == -1 && dataSource.getSize() > 0)
			// dataSource.setSelectedIndices(0);
		}

		/**
		 * Propagates {@link DataModelEvent events} to {@link SelectionListDataSource}
		 * 
		 * @see org.mozzes.swing.mgf.datamodel.events.DataModelEventListener#handleDataModelEvent(org.mozzes.swing.mgf.datamodel.DataModel,
		 *      org.mozzes.swing.mgf.datamodel.events.DataModelEvent)
		 */
		@Override
		public void handleDataModelEvent(DataModel<T> model, DataModelEvent<T> event) {
			if (event instanceof FieldValueUpdatedEvent<?>) {
				FieldValueUpdatedEvent<T> fvuEvent = (FieldValueUpdatedEvent<T>) event;
				dataSource.fireObjectsUpdatedEvent(fvuEvent.getForObject());
			}
			if (event instanceof FieldRemovedEvent<?> || event instanceof FieldMovedEvent<?>
					|| event instanceof FieldAddedEvent<?>) {
				throw new IllegalStateException("Model has to have exactly one field!");
			}
		}

		private void rowUpdated(ObjectReplacedEvent<T> event) {
			fireContentsChanged(this, event.getIndex(), event.getIndex());
		}

		private void rowsUpdated(@SuppressWarnings("unused") ObjectsUpdatedEvent<T> event) {
			// fireContentsChanged(this, event.getIndex(), event.getIndex() + event.getObjects().size() - 1);
			fireAllRowsUpdated();
		}

		private void fireAllRowsUpdated() {
			fireContentsChanged(this, 0, getLastIndex());
		}

		private void rowsDeleted(@SuppressWarnings("unused") ObjectsRemovedEvent<T> event) {
			fireIntervalRemoved(this, 0, getLastIndex());
		}

		private int getLastIndex() {
			int to = getSize() - 1;
			to = to >= 0 ? to : 0;
			return to;
		}

		private void rowsAdded(ObjectsAddedEvent<T> event) {
			int to = event.getIndex() + event.getObjects().size() - 1;
			if (to <= 0) {
				return;
			}
			fireIntervalAdded(this, event.getIndex(), to);
		}
	}
}
