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
package org.mozzes.swing.mgf.datarenderer.table.filter;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.mozzes.swing.mgf.datamodel.DataModel;
import org.mozzes.swing.mgf.datamodel.Field;
import org.mozzes.swing.mgf.datamodel.events.DataModelEvent;
import org.mozzes.swing.mgf.datamodel.events.DataModelEventListener;
import org.mozzes.swing.mgf.datamodel.events.FieldAddedEvent;
import org.mozzes.swing.mgf.datamodel.events.FieldRemovedEvent;
import org.mozzes.swing.mgf.datamodel.fields.PropertyField;
import org.mozzes.swing.mgf.datarenderer.combobox.ComboBoxRenderModel;
import org.mozzes.swing.mgf.datarenderer.table.TableRenderModel;
import org.mozzes.swing.mgf.datasource.SelectionListDataSource;
import org.mozzes.swing.mgf.datasource.events.selectionlist.SelectionChangedAdapter;
import org.mozzes.swing.mgf.datasource.events.selectionlist.SelectionChangedEvent;
import org.mozzes.swing.mgf.datasource.impl.DefaultSelectionListDataSource;
import org.mozzes.utils.filtering.Filter;
import org.mozzes.utils.filtering.FilterContainer;
import org.mozzes.utils.filtering.FilterWrapper;

import net.miginfocom.swing.MigLayout;


public class FilterComponentBuilder<T> {
	private JPanel panel;
	private JTextField txtSearchField = new JTextField();
	// private JLabel lblFilter = new JLabel("Filter: ");
	private Map<Field<T, ?>, ColumnFilterWrapper<?>> filterMapping =
			new HashMap<Field<T, ?>, ColumnFilterWrapper<?>>();

	@SuppressWarnings("unchecked")
	private SelectionListDataSource<Field<T, ?>> srcSearchColumns =
			new DefaultSelectionListDataSource<Field<T, ?>>(
			(Class<Field<T, ?>>) (Class<?>) Field.class);

	private ComboBoxRenderModel<Field<T, ?>> searchColumnRM =
			new ComboBoxRenderModel<Field<T, ?>>(srcSearchColumns,
			new PropertyField<Field<T, ?>, String>(String.class, "header"));
	// Maybe replace PropertyField with this CalculatedField
	// new CalculatedField<Field<T, ?>, String>(String.class) {
	// @Override
	// public String getValue(Field<T, ?> object) {
	// return StringUtils.capitalize(object.getHeader().toLowerCase());
	// }
	// }

	private final TableRenderModel<T> renderModel;
	private final FilterWrapper<T> filterContainer = new FilterContainer<T>();
	private final DefaultStringFilter defaultStringFilter = new DefaultStringFilter();

	public FilterComponentBuilder(TableRenderModel<T> renderModel) {
		this.renderModel = renderModel;
		renderModel.getFilteringFacility().addFilter(filterContainer);

		setupDataModel(renderModel);
		setupHandlers();
	}

	private void setupDataModel(TableRenderModel<T> renderModel) {
		renderModel.getDataModel().addEventListener(new DataModelEventListener<T>() {
			@Override
			public void handleDataModelEvent(DataModel<T> model, DataModelEvent<T> event) {
				if (event instanceof FieldAddedEvent<?>) {
					Field<T, ?> field = ((FieldAddedEvent<T>) event).getField();
					if (field.getFieldType().equals(String.class))
						srcSearchColumns.add(field);
				} else if (event instanceof FieldRemovedEvent<?>) {
					Field<T, ?> field = ((FieldRemovedEvent<T>) event).getField();
					srcSearchColumns.remove(field);
				}
			}
		});
		repopulateSearchColumns();
	}

	private void setupHandlers() {
		SearchChange searchChange = new SearchChange();
		txtSearchField.getDocument().addDocumentListener(searchChange);
		srcSearchColumns.addEventListener(searchChange);
	}

	private void repopulateSearchColumns() {
		srcSearchColumns.clear();
		for (Field<T, ?> field : renderModel.getDataModel().getFields()) {
			if (String.class.equals(field.getFieldType())) {
				srcSearchColumns.add(field);
			}
		}
		if (srcSearchColumns.getSize() != 0) {
			srcSearchColumns.setSelectedIndices(0);
		}
	}

	private void applyFilter(Filter<T> filter) {
		filterContainer.setWrappedFilter(filter);
		renderModel.getFilteringFacility().applyFilter();
	}

	@SuppressWarnings("unchecked")
	public void applyFilter() {
		Field<T, ?> field = srcSearchColumns.getSelectedValue();
		if (field == null) {
			applyFilter(null);
			return;
		}

		Filter<T> filter = filterMapping.get(field) != null ? filterMapping.get(field) :
				new ColumnFilterWrapper<String>(defaultStringFilter, (Field<T, String>) field);
		applyFilter(filter);
	}

	/**
	 * Setup and apply filter function for specified column
	 * 
	 * @param <F> Type of a value contained in column
	 * @param column Index of the column
	 * @param type Class of a value contained in column
	 * @param filter Filter function
	 */
	public <F> void setFilterFor(int column, Class<F> type, ColumnFilter<T, F> filter) {
		Field<T, F> field = renderModel.getDataModel().getField(column, type);
		if (!srcSearchColumns.getData().contains(field))
			srcSearchColumns.add(field);

		ColumnFilterWrapper<F> wrapper = new ColumnFilterWrapper<F>(filter, field);
		filterMapping.put(field, wrapper);
	}

	/**
	 * Removes filter function for specified column
	 * 
	 * @param column Index of a column
	 */
	public void removeFilterFor(int column) {
		Field<T, ?> field = renderModel.getDataModel().getField(column);
		if (srcSearchColumns.getData().contains(field))
			srcSearchColumns.remove(field);
		filterMapping.remove(field);
		if (srcSearchColumns.getSelectedIndex() == -1 && srcSearchColumns.getSize() > 0)
			srcSearchColumns.setSelectedIndices(0);
		applyFilter();
	}

	/**
	 * @return {@link JPanel} with all necessary components for filtering
	 */
	public JComponent getRenderComponent() {
		if (panel == null) {
			panel = new JPanel(new MigLayout("wrap 2", "[grow, fill][p!]"));
			// panel.add(lblFilter);
			panel.add(txtSearchField);
			panel.add(searchColumnRM.getRenderComponent());
			applyFilter();
		}
		return panel;
	}

	/**
	 * Listener for all important user actions
	 * 
	 * @author milos
	 */
	private class SearchChange extends SelectionChangedAdapter<Field<T, ?>>
			implements DocumentListener {

		@Override
		public void changedUpdate(DocumentEvent e) {
			applyFilter();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			applyFilter();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			applyFilter();
		}

		@Override
		public void selectionChanged(SelectionListDataSource<Field<T, ?>> source,
				SelectionChangedEvent<Field<T, ?>> event) {
			applyFilter();
		}

	}

	/**
	 * Filter for string columns<br>
	 * Pass if cell vale starts with specified criteria string (non case-sensitive)
	 * 
	 * @author milos
	 */
	private class DefaultStringFilter implements ColumnFilter<T, String> {
		@Override
		public boolean isAcceptable(T object, String fieldValue, String criteria) {
			if (object == null || fieldValue == null || fieldValue.isEmpty())
				return false;
			return fieldValue.toLowerCase().startsWith(criteria.toLowerCase());
		}
	}

	/**
	 * Adapter which converts {@link ColumnFilter} to {@link Filter}S
	 * 
	 * @author milos
	 * 
	 * @param <F> Type of a column value
	 */
	private class ColumnFilterWrapper<F> implements Filter<T> {
		private final ColumnFilter<T, F> filter;
		private final Field<T, F> field;

		public ColumnFilterWrapper(ColumnFilter<T, F> filter, Field<T, F> field) {
			this.filter = filter;
			this.field = field;
		}

		@Override
		public boolean isAcceptable(T object) {
			return filter.isAcceptable(object, field.getValue(object), txtSearchField.getText());
		}
	}
}
