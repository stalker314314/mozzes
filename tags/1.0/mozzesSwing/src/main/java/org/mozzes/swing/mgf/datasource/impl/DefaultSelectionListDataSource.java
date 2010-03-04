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
package org.mozzes.swing.mgf.datasource.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.mozzes.swing.mgf.datasource.BeanDataSource;
import org.mozzes.swing.mgf.datasource.DataSource;
import org.mozzes.swing.mgf.datasource.ListDataSource;
import org.mozzes.swing.mgf.datasource.SelectionListDataSource;
import org.mozzes.swing.mgf.datasource.events.DataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.SourceChangedEvent;
import org.mozzes.swing.mgf.datasource.events.bean.BeanDataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.bean.BeanDataSourceEventListener;
import org.mozzes.swing.mgf.datasource.events.bean.DataUpdatedEvent;
import org.mozzes.swing.mgf.datasource.events.list.ListDataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.list.ListEventAdapter;
import org.mozzes.swing.mgf.datasource.events.list.ObjectsAddedEvent;
import org.mozzes.swing.mgf.datasource.events.list.ObjectsRemovedEvent;
import org.mozzes.swing.mgf.datasource.events.list.ObjectsUpdatedEvent;
import org.mozzes.swing.mgf.datasource.events.selectionlist.SelectionChangedEvent;
import org.mozzes.swing.mgf.datasource.events.selectionlist.SelectionListDataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.selectionlist.SelectionListEventAdapter;
import org.mozzes.swing.mgf.datasource.events.selectionlist.SelectionModeChangedEvent;


/**
 * Default {@link SelectionListDataSource} implementation
 * 
 * @author milos
 * 
 * @param <T> The type of the bean contained by the provided list
 */
public class DefaultSelectionListDataSource<T> extends DefaultListDataSource<T> implements SelectionListDataSource<T> {
	private static final long serialVersionUID = 15L;
	private static final String unsupportedMessage = "This is readonly data-source!";

	private SelectionMode selectionMode = SelectionMode.SINGLE_SELECTION;
	private final List<Integer> selected = new LinkedList<Integer>();

	private final ListDataSource<Integer> selectedIndicesDataSource =
			new DefaultListDataSource<Integer>(Integer.class, new LinkedList<Integer>());
	private final ListDataSource<T> selectedObjectsDataSource =
			new SelectedObjectsDataSource();

	private final SelectedIndexDataSource selectedIndexDataSource = new SelectedIndexDataSource();
	private final SelectedObjectDataSource selectedObjectDataSource = new SelectedObjectDataSource();

	private SelectedObjectsDSPropagator selectedObjectsDSPropagator;
	private SelectedObjectDSPropagator selectedObjectDSPropagator;

	private final SelfHandlers selfHandlers = new SelfHandlers();
	private final SelectionSourceHandlers selectionSourceHandlers = new SelectionSourceHandlers();
	private final SelectionBinding selectionBinding = new SelectionBinding();
	private SelectionListDataSource<T> selectionBoundTo = null;

	/**
	 * Constructs the source with empty list as source
	 */
	public DefaultSelectionListDataSource(Class<T> elementType) {
		super(elementType);
		this.addEventListener(selfHandlers);
		selectedIndicesDataSource.addEventListener(selectionSourceHandlers);
	}

	/**
	 * @param dataSource Another source to use as the source for this one (chaining)
	 */
	public DefaultSelectionListDataSource(Class<T> elementType, DataSource<List<T>> dataSource) {
		super(elementType, dataSource);
		this.addEventListener(selfHandlers);
		selectedIndicesDataSource.addEventListener(selectionSourceHandlers);
	}

	/**
	 * @param dataSource Another source to use as the source for this one (chaining)
	 */
	public DefaultSelectionListDataSource(ListDataSource<T> dataSource) {
		this(dataSource.getElementType(), dataSource);
	}

	/**
	 * @param data {@link List} that should be provided
	 */
	public DefaultSelectionListDataSource(Class<T> elementType, List<T> data) {
		super(elementType, data);
		this.addEventListener(selfHandlers);
		selectedIndicesDataSource.addEventListener(selectionSourceHandlers);
	}

	public DefaultSelectionListDataSource(Class<T> elementType, T[] data) {
		super(elementType, data);
		this.addEventListener(selfHandlers);
		selectedIndicesDataSource.addEventListener(selectionSourceHandlers);
	}

	@Override
	protected List<Class<?>> getSupportedBaseEvents() {
		List<Class<?>> supported = super.getSupportedBaseEvents();
		supported.add(SelectionListDataSourceEvent.class);
		return supported;
	}

	@Override
	public void addSelectedIndices(int... indices) {
		if (getSelectionMode() == SelectionMode.SINGLE_SELECTION)
			throw new UnsupportedOperationException("Cannot add selection in SINGLE_SELECTION mode");

		List<Integer> newSelection = new ArrayList<Integer>(selected);
		for (int index : indices) {
			if (index < 0 || index >= getSize()) {
				throw new IndexOutOfBoundsException("Selection index(" + index + ") out of bounds.");
			}
			if (!newSelection.contains(index))
				newSelection.add(index);
		}
		applySelection(newSelection);
	}

	@Override
	public void addSelectionInterval(int from, int to) {
		if (getSelectionMode() == SelectionMode.SINGLE_SELECTION)
			throw new UnsupportedOperationException("Cannot add selection in SINGLE_SELECTION mode");
		int[] indices = getIntervalIndices(from, to);
		addSelectedIndices(indices);
	}

	@Override
	public void clearSelection() {
		selected.clear();
		fireEvent(new SelectionChangedEvent<T>());
	}

	@Override
	public int getSelectedIndex() {
		return selected.size() == 0 ? -1 : selected.get(0);
	}

	@Override
	public List<Integer> getSelectedIndices() {
		return Collections.unmodifiableList(new ArrayList<Integer>(selected));
	}

	@Override
	public T getSelectedValue() {
		return selected.isEmpty() ? null : get(getSelectedIndex());
	}

	@Override
	public List<T> getSelectedValues() {
		return Collections.unmodifiableList(new ArrayList<T>(selectedObjectsDataSource.getData()));
	}

	@Override
	public SelectionMode getSelectionMode() {
		return selectionMode;
	}

	@Override
	public void invertSelection() {
		List<Integer> newSelection = new ArrayList<Integer>(selected);
		for (int i = 0; i < getSize(); i++) {
			if (newSelection.contains(i))
				newSelection.remove((Integer) i);
			else
				newSelection.add(i);
		}
		applySelection(newSelection);
	}

	private void applySelection(List<Integer> newSelection) {
		applySelection(newSelection, true);
	}

	private void applySelection(List<Integer> newSelection, boolean checkForChange) {
		checkAndCorrectSelectionData(newSelection);
		if (checkForChange && newSelection.equals(selected))
			return;
		selected.clear();
		selected.addAll(newSelection);
		fireEvent(new SelectionChangedEvent<T>());
	}

	@Override
	public void removeSelectedIndices(int... indices) {
		List<Integer> newSelection = new ArrayList<Integer>(selected);
		for (int index : indices) {
			if (index < 0 || index >= getSize()) {
				throw new IndexOutOfBoundsException("Selection index(" + index + ") out of bounds.");
			}
			newSelection.remove((Object) index);
		}
		applySelection(newSelection);
	}

	@Override
	public void removeSelectionInterval(int from, int to) {
		int[] indices = getIntervalIndices(from, to);
		removeSelectedIndices(indices);
	}

	private int[] getIntervalIndices(int from, int to) {
		int realFrom = from;
		int realTo = to;
		if (realFrom > realTo) {
			int tmp = realFrom;
			realFrom = realTo;
			realTo = tmp;
		}
		if (realFrom < 0 || realTo > getSize())
			throw new IndexOutOfBoundsException("Interval out of bounds!");

		int[] indices = new int[realTo - realFrom + 1];
		for (int i = realFrom, j = 0; i <= realTo; i++, j++) {
			indices[j] = i;
		}
		return indices;
	}

	@Override
	public void selectAll() {
		if (getSelectionMode() == SelectionMode.SINGLE_SELECTION) {
			throw new UnsupportedOperationException("Cannot select more than one object in SINGLE_SELECTION mode!");
		}

		List<Integer> newSelection = new ArrayList<Integer>();
		for (int i = 0; i < getSize(); i++) {
			newSelection.add(i);
		}
		applySelection(newSelection);
	}

	@Override
	public void setSelectedIndices(int... indices) {
		if (getSelectionMode() == SelectionMode.SINGLE_SELECTION && indices.length > 1) {
			throw new IllegalArgumentException("Cannot select more than one object in SINGLE_SELECTION mode!");
		}

		List<Integer> newSelection = new ArrayList<Integer>();
		for (int index : indices) {
			if (!newSelection.contains(index))
				newSelection.add(index);
		}
		applySelection(newSelection);
	}

	@Override
	public void setSelectedIndices(List<Integer> selectedIndices) {
		int[] indices = new int[selectedIndices.size()];
		for (int i = 0; i < indices.length; i++) {
			indices[i] = selectedIndices.get(i);
		}
		setSelectedIndices(indices);
	}

	@Override
	public void setSelectionInterval(int from, int to) {
		selected.clear();
		addSelectionInterval(from, to);
	}

	@Override
	public void setSelectionMode(SelectionMode mode) {
		clearSelection();
		this.selectionMode = mode;
		fireEvent(new SelectionModeChangedEvent<T>());
	}

	@Override
	public void toggleSelectedIndices(int... indices) {
		List<Integer> newSelected = new ArrayList<Integer>(selected);
		for (int index : indices) {
			if (newSelected.contains(index)) {
				newSelected.remove((Integer) index);
			} else {
				newSelected.add(index);
			}
		}
		applySelection(newSelected);
	}

	@Override
	protected void fireEvent(DataSourceEvent<List<T>> event) {
		boolean isSelectionEvent = SelectionListDataSourceEvent.class.isAssignableFrom(event.getClass());
		if (event.isPropagated() && isSelectionEvent) {
			return;
		}
		List<Integer> modifiedSelection = null;

		// Change selection before event - too complicated to make it work with chained selections
		// if (!isSelectionEvent) {
		// modifiedSelection = getSelectionModifiedForEvent(event);
		// if (modifiedSelection != null) {
		// applySelection(modifiedSelection);
		// }
		// }
		// super.fireEvent(event);

		// Change selection after event
		if (!isSelectionEvent) {
			modifiedSelection = getSelectionModifiedForEvent(event);
			if (modifiedSelection != null) {
				selfHandlers.enabled = false;
				selected.clear();
			}
		}
		super.fireEvent(event);
		if (!isSelectionEvent && modifiedSelection != null) {
			selfHandlers.enabled = true;
			applySelection(modifiedSelection, false);
		}
	}

	private List<Integer> getSelectionModifiedForEvent(DataSourceEvent<?> event) {
		if (selected == null || selected.isEmpty())
			return null;

		if (event instanceof ObjectsRemovedEvent<?> ||
				event instanceof SourceChangedEvent<?> ||
				event instanceof DataUpdatedEvent<?>) {
			return new LinkedList<Integer>();
		}

		if (event instanceof ObjectsUpdatedEvent<?>) {
			return null;
		}

		List<Integer> newSelection = new LinkedList<Integer>(selected);
		if (event instanceof ObjectsAddedEvent<?>) {
			ObjectsAddedEvent<?> objectsAddedEvent = (ObjectsAddedEvent<?>) event;
			for (int i = 0; i < newSelection.size(); i++) {
				if (newSelection.get(i) >= objectsAddedEvent.getIndex()) {
					newSelection.set(i, newSelection.get(i) + objectsAddedEvent.getObjects().size());
				}
			}
		}

		try {
			if (newSelection.equals(selected))
				return null;
			return newSelection;
		} catch (Throwable e) {
			return new LinkedList<Integer>();
		}
	}

	@Override
	public ListDataSource<Integer> getSelectedIndicesDataSource() {
		return selectedIndicesDataSource;
	}

	@Override
	public ListDataSource<T> getSelectedValuesDataSource() {
		return selectedObjectsDataSource;
	}

	@Override
	public BeanDataSource<Integer> getSelectedIndexDataSource() {
		return selectedIndexDataSource;
	}

	@Override
	public BeanDataSource<T> getSelectedValueDataSource() {
		return selectedObjectDataSource;
	}

	private void checkAndCorrectSelectionData(List<Integer> selectionData) {
		if (selectionData == null)
			throw new IllegalArgumentException("Selection cannot be null!");
		if (getSelectionMode() == SelectionMode.SINGLE_SELECTION && selectionData.size() > 1)
			throw new IllegalArgumentException("Cannot select more than one object in SINGLE_SELECTION mode!");

		Collections.sort(selectionData);

		ArrayList<Integer> newList = new ArrayList<Integer>(selectionData.size());
		// Integer previous = null;
		for (Integer index : selectionData) {
			Integer previous = newList.isEmpty() ? null : newList.get(newList.size() - 1);
			if (index == null)
				throw new IllegalArgumentException("An index cannot be null!");
			if (index < 0 || index >= getSize())
				throw new IndexOutOfBoundsException("An index is out of bounds!");
			if (index.equals(previous))
				// if (selectionData.indexOf(index) != selectionData.lastIndexOf(index))
				// throw new IllegalArgumentException("Cannot have duplicate index selections!");
				continue;
			newList.add(index);
			// previous = index;
		}
		selectionData.clear();
		selectionData.addAll(newList);
	}

	public void bindSelection(SelectionListDataSource<T> other) {
		// this.getSelectedIndicesDataSource().setDataSource(other.getSelectedIndicesDataSource());
		if (other == this) {
			throw new IllegalArgumentException("Cannot bind selection to self!");
		}
		if (selectionBoundTo != null) {
			selectionBoundTo.removeEventListener(selectionBinding);
			this.removeEventListener(selectionBinding);
		}
		selectionBoundTo = other;
		if (selectionBoundTo == null)
			return;
		selectionBoundTo.addEventListener(selectionBinding);
		this.addEventListener(selectionBinding);
	}

	private class SelectionSourceHandlers extends ListEventAdapter<Integer> {

		private boolean enabled = true;

		@Override
		public void handleDataSourceEvent(ListDataSource<Integer> source, ListDataSourceEvent<Integer> event) {
			if (!enabled)
				return;
			checkAndCorrectSelectionData(source.getData());
			if (selected.equals(source.getData()))
				return;
			selectedIndicesDataSource.fireDataUpdatedEvent();
		}

		@Override
		public void handleDataSourceEvent(DataSource<List<Integer>> source, DataSourceEvent<List<Integer>> event) {
			propagate(source);
		}

		@Override
		public void handleDataSourceEvent(BeanDataSource<List<Integer>> source, BeanDataSourceEvent<List<Integer>> event) {
			propagate(source);
		}

		private void propagate(DataSource<List<Integer>> source) {
			if (enabled) {
				checkAndCorrectSelectionData(source.getData());
				if (selected.equals(source.getData()))
					return;

				selfHandlers.enabled = false;
				selected.clear();
				selected.addAll(source.getData());
				fireEvent(new SelectionChangedEvent<T>());
				selfHandlers.enabled = true;
			}
			selectedObjectsDSPropagator.enabled = false;
			selectedObjectsDataSource.fireDataUpdatedEvent();
			selectedObjectsDSPropagator.enabled = true;
			selectedIndexDataSource.syncSelection();
		}
	}

	private class SelfHandlers extends SelectionListEventAdapter<T> {
		private boolean enabled = true;
		private DataSource<?> fromDataSource = null;

		@Override
		public void handleDataSourceEvent(SelectionListDataSource<T> source, SelectionListDataSourceEvent<T> event) {
			if (!enabled)
				return;
			selectionSourceHandlers.enabled = false;
			selectedIndicesDataSource.getData().clear();
			selectedIndicesDataSource.getData().addAll(selected);
			selectedIndicesDataSource.fireDataUpdatedEvent();
			selectionSourceHandlers.enabled = true;
		}

		@Override
		public void handleDataSourceEvent(ListDataSource<T> source, ListDataSourceEvent<T> event) {
			// Propagate to selection sources
			if (event instanceof ObjectsUpdatedEvent<?>)
				propagate();
		}

		@Override
		public void handleDataSourceEvent(BeanDataSource<List<T>> source, BeanDataSourceEvent<List<T>> event) {
			// Propagate to selection sources
			if (event instanceof DataUpdatedEvent<?>)
				propagate();
		}

		@Override
		public void handleDataSourceEvent(DataSource<List<T>> source, DataSourceEvent<List<T>> event) {
			// Propagate to selection sources
			propagate();
		}

		private void propagate() {
			if (!enabled)
				return;
			if (fromDataSource != selectedObjectsDataSource) {
				selectedObjectsDSPropagator.enabled = false;
				selectedObjectsDataSource.fireObjectsUpdatedEvent();
				selectedObjectsDSPropagator.enabled = true;
			}
			if (fromDataSource != selectedObjectDataSource) {
				selectedObjectDSPropagator.enabled = false;
				selectedObjectDataSource.fireDataUpdatedEvent();
				selectedObjectDSPropagator.enabled = true;
			}
		}
	}

	private class SelectedObjectsDataSource extends DefaultListDataSource<T> {
		private static final long serialVersionUID = 15L;

		public SelectedObjectsDataSource() {
			super(DefaultSelectionListDataSource.this.getElementType(), (List<T>) null);
			if (selectedObjectsDSPropagator == null)
				selectedObjectsDSPropagator = new SelectedObjectsDSPropagator();
			this.addEventListener(selectedObjectsDSPropagator);
		}

		@Override
		public List<T> getData() {
			List<T> data = new ArrayList<T>();
			for (Integer i : getSelectedIndices()) {
				data.add(get(i));
			}
			return Collections.unmodifiableList(data);
		}

		@Override
		public T get(int index) {
			return DefaultSelectionListDataSource.this.get(index);
		}

		@Override
		public void add(Collection<T> elements) {
			throw new UnsupportedOperationException(unsupportedMessage);
		}

		@Override
		public void add(int index, Collection<T> elements) {
			throw new UnsupportedOperationException(unsupportedMessage);
		}

		@Override
		public void add(int index, T... elements) {
			throw new UnsupportedOperationException(unsupportedMessage);
		}

		@Override
		public void add(int index, T element) {
			throw new UnsupportedOperationException(unsupportedMessage);
		}

		@Override
		public void add(T... elements) {
			throw new UnsupportedOperationException(unsupportedMessage);
		}

		@Override
		public void add(T object) {
			throw new UnsupportedOperationException(unsupportedMessage);
		}

		@Override
		public boolean remove(Collection<T> elements) {
			throw new UnsupportedOperationException(unsupportedMessage);
		}

		@Override
		public T remove(int index) {
			throw new UnsupportedOperationException(unsupportedMessage);
		}

		@Override
		public boolean remove(T... elements) {
			throw new UnsupportedOperationException(unsupportedMessage);
		}

		@Override
		public boolean remove(T object) {
			throw new UnsupportedOperationException(unsupportedMessage);
		}

		@Override
		public void clear() {
			throw new UnsupportedOperationException(unsupportedMessage);
		}

		@Override
		public T set(int index, T element) {
			throw new UnsupportedOperationException(unsupportedMessage);
		}

		@Override
		public void bindTo(DataSource<List<T>> dataSource) {
			throw new UnsupportedOperationException(unsupportedMessage);
		}

		@Override
		public void setData(List<T> data) {
			throw new UnsupportedOperationException(unsupportedMessage);
		}

		@Override
		protected void fireEvent(DataSourceEvent<List<T>> event) {
			if (!(event instanceof DataUpdatedEvent<?>)
					&& !ObjectsUpdatedEvent.class.isAssignableFrom(event.getClass()))
				throw new IllegalStateException(
						"Only DataUpdated and ObjectsUpdated events should be fired by this source!");
			super.fireEvent(event);
		}
	}

	private class SelectedObjectDataSource extends DefaultBeanDataSource<T> {
		private static final long serialVersionUID = 15L;

		public SelectedObjectDataSource() {
			super(DefaultSelectionListDataSource.this.getElementType(), (T) null);
			if (selectedObjectDSPropagator == null)
				selectedObjectDSPropagator = new SelectedObjectDSPropagator();
			this.addEventListener(selectedObjectDSPropagator);
		}

		@Override
		protected void fireEvent(DataSourceEvent<T> event) {
			super.fireEvent(event);
		}

		@Override
		public T getData() {
			return getSelectedValue();
		}

		@Override
		public void bindTo(DataSource<T> dataSource) {
			throw new UnsupportedOperationException(unsupportedMessage);
		}

		@Override
		public void setData(T data) {
			throw new UnsupportedOperationException(unsupportedMessage);
		}
	}

	private class SelectedIndexDataSource extends DefaultBeanDataSource<Integer> {
		private static final long serialVersionUID = 15L;
		private int selectedIndex = getSelectedIndex();

		public SelectedIndexDataSource() {
			super(Integer.class, (Integer) null);
		}

		@Override
		public Integer getData() {
			return selectedIndex;
		}

		void syncSelection() {
			if (getSelectedIndex() == selectedIndex)
				return;
			selectedIndex = getSelectedIndex();
			this.fireDataUpdatedEvent();
			selectedObjectDSPropagator.enabled = false;
			selectedObjectDataSource.fireDataUpdatedEvent();
			selectedObjectDSPropagator.enabled = true;
		}

		@Override
		public void bindTo(DataSource<Integer> dataSource) {
			throw new UnsupportedOperationException(unsupportedMessage);
		}

		@Override
		public void setData(Integer data) {
			throw new UnsupportedOperationException(unsupportedMessage);
		}
	}

	private class SelectedObjectsDSPropagator extends ListEventAdapter<T> {
		private boolean enabled = true;

		@Override
		public void handleDataSourceEvent(ListDataSource<T> source, ListDataSourceEvent<T> event) {
			// Only possible event here is ObjectsUpdated
			propagate();
		}

		@Override
		public void handleDataSourceEvent(DataSource<List<T>> source, DataSourceEvent<List<T>> event) {
			// Only possible event here is DataUpdatedEvent
			propagate();
		}

		@Override
		public void handleDataSourceEvent(BeanDataSource<List<T>> source, BeanDataSourceEvent<List<T>> event) {
			propagate();
		}

		private void propagate() {
			if (!enabled)
				return;
			selfHandlers.fromDataSource = selectedObjectsDataSource;
			DefaultSelectionListDataSource.this.fireObjectsUpdatedEvent(getSelectedValues());
			selfHandlers.fromDataSource = null;
		}
	}

	private class SelectedObjectDSPropagator implements BeanDataSourceEventListener<T> {
		private boolean enabled = true;

		@Override
		public void handleDataSourceEvent(BeanDataSource<T> source, BeanDataSourceEvent<T> event) {
			propagate();
		}

		@Override
		public void handleDataSourceEvent(DataSource<T> source, DataSourceEvent<T> event) {
			propagate();
		}

		private void propagate() {
			if (!enabled)
				return;
			selfHandlers.fromDataSource = selectedObjectDataSource;
			DefaultSelectionListDataSource.this.fireObjectsUpdatedEvent(getSelectedValue());
			selfHandlers.fromDataSource = null;
		}
	}

	private class SelectionBinding extends SelectionListEventAdapter<T> {
		private boolean enabled = true;

		@Override
		public void handleDataSourceEvent(SelectionListDataSource<T> source, SelectionListDataSourceEvent<T> event) {
			if (!enabled)
				return;
			if (!(event instanceof SelectionChangedEvent<?>))
				return;

			SelectionListDataSource<T> propagateTo = source == selectionBoundTo ?
					DefaultSelectionListDataSource.this
					: selectionBoundTo;
			enabled = false;
			propagateTo.getSelectedIndicesDataSource().getData().clear();
			List<Integer> selection = new ArrayList<Integer>(source.getSelectedIndices());
			propagateTo.getSelectionMode().filterSelection(selection);
			propagateTo.getSelectedIndicesDataSource().add(selection);
			enabled = true;
		}
	}

}
