package org.mozzes.swing.mgf.datarenderer.list;

import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;

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
 * Custom {@link ListModel} which is bound to a data source and reacts to its events properly
 * 
 * @author milos
 * 
 * @param <T> Type of the bean contained in one row of the list using this model
 */
class CustomListModel<T> extends AbstractListModel {
	private static final long serialVersionUID = 15L;

	private boolean selectionPropagationEnabled = true;

	private ListDataSource<T> dataSource;
	private final DataModel<T> dataModel;

	private final InternalHandlers handlers = new InternalHandlers();

	/**
	 * @param model {@link DataModel} to be used to present the data
	 */
	public CustomListModel(DataModel<T> model) {
		this.dataModel = model;
		this.setupDataModel();
	}

	/**
	 * @param model {@link DataModel} to be used to present the data
	 * @param source {@link ListDataSource Data source} to be used to provide rows
	 */
	public CustomListModel(DataModel<T> model, ListDataSource<T> source) {
		this.dataModel = model;
		this.setupDataModel();
		this.setDataSource(source);
	}

	@Override
	public Object getElementAt(int index) {
		if (dataSource == null) {
			throw new IllegalStateException("DataSource cannot be null!");
		}
		if (dataModel.getFieldsNumber() != 1)
			throw new IllegalStateException("Model has to have exactly one field!");

		return dataModel.getFieldValue(0, dataSource.get(index));
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
	 * Attaches all necessary listeners to the {@link DataModel}<br>
	 * <b>[NOTICE] this method should only be called once during the construction of {@link CustomListModel}</b>
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
	 * Sets the specified {@link ListDataSource source} to be used for feeding the data to the {@link CustomListModel
	 * list model} and attaches all necessary listeners to it
	 * 
	 * @param source {@link ListDataSource source} to be used for feeding the data to the {@link CustomListModel list
	 *            model}
	 */
	public void setDataSource(ListDataSource<T> source) {
		if (dataSource != null)
			dataSource.removeEventListener(handlers);
		dataSource = source;
		if (dataSource == null)
			return;
		dataSource.addEventListener(handlers);
	}

	/**
	 * @return {@link ListDataSource Data source} in use by this {@link CustomListModel list model} instance
	 */
	public ListDataSource<T> getDataSource() {
		return dataSource;
	}

	/**
	 * @return {@link DataModel} in use by this {@link CustomListModel list model} instance
	 */
	public DataModel<T> getDataModel() {
		return dataModel;
	}

	/**
	 * @return True if the propagation should be done, false otherwise
	 */
	boolean isSelectionPropagationEnabled() {
		return selectionPropagationEnabled;
	}

	/**
	 * Coordinates propagation of events between the {@link ListDataSource data source}, {@link DataModel data model}
	 * and {@link ListModel list model}
	 * 
	 * @author milos
	 */
	private class InternalHandlers extends ListEventAdapter<T> implements DataModelEventListener<T> {

		/**
		 * Propagates {@link ListDataSourceEvent events} to {@link ListModel}
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
		 * Propagates {@link DataSourceEvent events} to {@link ListModel}
		 * 
		 * @see org.mozzes.swing.mgf.datasource.events.DataSourceEventListener#handleDataSourceEvent(org.mozzes.swing.mgf.datasource.DataSource,
		 *      org.mozzes.swing.mgf.datasource.events.DataSourceEvent)
		 */
		@Override
		public void handleDataSourceEvent(BeanDataSource<List<T>> source, BeanDataSourceEvent<List<T>> event) {
			selectionPropagationEnabled = false;
			if (event instanceof DataUpdatedEvent<?>)
				fireAllRowsUpdated();
			selectionPropagationEnabled = true;
		}

		/**
		 * Propagates {@link DataSourceEvent events} to {@link ListModel}
		 * 
		 * @see org.mozzes.swing.mgf.datasource.events.DataSourceEventListener#handleDataSourceEvent(org.mozzes.swing.mgf.datasource.DataSource,
		 *      org.mozzes.swing.mgf.datasource.events.DataSourceEvent)
		 */
		@Override
		public void handleDataSourceEvent(DataSource<List<T>> source, DataSourceEvent<List<T>> event) {
			selectionPropagationEnabled = false;
			if (event instanceof SourceChangedEvent<?>)
				fireAllRowsUpdated();
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
			if (event instanceof FieldValueUpdatedEvent<?>) {
				FieldValueUpdatedEvent<T> fvuEvent = (FieldValueUpdatedEvent<T>) event;
				dataSource.fireObjectsUpdatedEvent(fvuEvent.getForObject());
			}
			if (event instanceof FieldRemovedEvent<?> || event instanceof FieldMovedEvent<?>
					|| event instanceof FieldAddedEvent<?>) {
				throw new IllegalStateException("Model cannot be changed at runtime!");
			}
		}

		private void rowUpdated(ObjectReplacedEvent<T> event) {
			fireContentsChanged(this, event.getIndex(), event.getIndex());
		}

		private void rowsUpdated(@SuppressWarnings("unused") ObjectsUpdatedEvent<T> event) {
			// fireContentsChanged(this, event.getIndex(), event.getIndex() + event.getObjects().size() - 1);
			fireAllRowsUpdated();
		}

		private void rowsDeleted(@SuppressWarnings("unused") ObjectsRemovedEvent<T> event) {
			// fireIntervalRemoved(this, event.getIndex(), event.getIndex() + event.getObjects().size() - 1);
			fireIntervalRemoved(this, 0, getLastIndex());
		}

		private void rowsAdded(ObjectsAddedEvent<T> event) {
			fireIntervalAdded(this, event.getIndex(), event.getIndex() + event.getObjects().size() - 1);
		}

		private void fireAllRowsUpdated() {
			fireContentsChanged(this, 0, getLastIndex());
		}

		private int getLastIndex() {
			int to = getSize() - 1;
			to = to >= 0 ? to : 0;
			return to;
		}
	}
}
