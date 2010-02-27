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
package org.mozzes.swing.mgf.datarenderer.combobox;

import java.awt.Component;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.mozzes.swing.mgf.datamodel.DataModel;
import org.mozzes.swing.mgf.datamodel.Field;
import org.mozzes.swing.mgf.datamodel.fields.WholeObjectField;
import org.mozzes.swing.mgf.datamodel.impl.DefaultDataModel;
import org.mozzes.swing.mgf.datarenderer.DataRenderModel;
import org.mozzes.swing.mgf.datasource.DataSource;
import org.mozzes.swing.mgf.datasource.SelectionListDataSource;
import org.mozzes.swing.mgf.datasource.impl.DefaultSelectionListDataSource;
import org.mozzes.utils.reflection.ReflectionUtils;
import org.mozzes.utils.reflection.ReflectiveMethod;


/**
 * {@link DataRenderModel}
 * 
 * @author milos
 */
public class ComboBoxRenderModel<T> implements DataRenderModel<T, SelectionListDataSource<T>> {
	private static final long serialVersionUID = 15L;

	private final CustomComboBoxModel<T> comboModel;
	private JComboBox comboBox;
	private final ListCellRenderer wholeObjectRenderer;
	private final DataModel<T> dataModel;;

	// ############## Constructor Region ##############

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 */
	public ComboBoxRenderModel(Class<T> objectType) {
		this(new DefaultSelectionListDataSource<T>(objectType));
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param dataSource {@link DataSource Source} that will be used to provide data for the {@link DataModel model}
	 */
	public ComboBoxRenderModel(SelectionListDataSource<T> dataSource) {
		this(dataSource, new WholeObjectField<T>(dataSource.getElementType()));
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param data Data that will be presented by the {@link JComboBox ComboBox}
	 */
	@SuppressWarnings("unchecked")
	public ComboBoxRenderModel(T[] data) {
		this(new DefaultSelectionListDataSource<T>((Class<T>) data.getClass().getComponentType(), data));
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param field {@link Field} that will be used to populate the list with data
	 */
	public ComboBoxRenderModel(Class<T> objectType, Field<T, ?> field) {
		this(objectType, new DefaultDataModel<T>().addField(field));
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param dataModel {@link DataModel Model} that will be used to construct {@link DataRenderModel render model}
	 */
	public ComboBoxRenderModel(Class<T> objectType, DataModel<T> dataModel) {
		this(new DefaultSelectionListDataSource<T>(objectType), dataModel);
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param dataSource {@link DataSource Source} that will be used to provide data for the {@link DataModel model}
	 * @param field {@link Field} that will be used to populate the list with data
	 */
	public ComboBoxRenderModel(SelectionListDataSource<T> dataSource, Field<T, ?> field) {
		this(dataSource, new DefaultDataModel<T>().addField(field));
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param dataSource {@link DataSource Source} that will be used to provide data for the {@link DataModel model}
	 * @param dataModel {@link DataModel Model} that will be used to construct {@link DataRenderModel render model}
	 */
	public ComboBoxRenderModel(SelectionListDataSource<T> dataSource, DataModel<T> dataModel) {
		this(new JComboBox(), dataSource, dataModel);
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 */
	public ComboBoxRenderModel(Class<T> objectType, JComboBox comboBox) {
		this(comboBox, new DefaultSelectionListDataSource<T>(objectType));
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param dataSource {@link DataSource Source} that will be used to provide data for the {@link DataModel model}
	 */
	public ComboBoxRenderModel(JComboBox comboBox, SelectionListDataSource<T> dataSource) {
		this(comboBox, dataSource, new WholeObjectField<T>(dataSource.getElementType()));
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param field {@link Field} that will be used to populate the list with data
	 */
	public ComboBoxRenderModel(Class<T> objectType, JComboBox comboBox, Field<T, ?> field) {
		this(objectType, comboBox, new DefaultDataModel<T>().addField(field));
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param dataModel {@link DataModel Model} that will be used to construct {@link DataRenderModel render model}
	 */
	public ComboBoxRenderModel(Class<T> objectType, JComboBox comboBox, DataModel<T> dataModel) {
		this(comboBox, new DefaultSelectionListDataSource<T>(objectType), dataModel);
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param dataSource {@link DataSource Source} that will be used to provide data for the {@link DataModel model}
	 * @param field {@link Field} that will be used to populate the list with data
	 */
	public ComboBoxRenderModel(JComboBox comboBox, SelectionListDataSource<T> dataSource, Field<T, ?> field) {
		this(comboBox, dataSource, new DefaultDataModel<T>().addField(field));
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param comboBox {@link JComboBox} to be used for presenting data
	 * @param dataSource {@link DataSource Source} that will be used to provide data for the {@link DataModel model}
	 * @param dataModel {@link DataModel Model} that will be used to construct {@link DataRenderModel render model}
	 */
	public ComboBoxRenderModel(JComboBox comboBox, SelectionListDataSource<T> dataSource, DataModel<T> dataModel) {
		this(comboBox, dataSource, dataModel, true);
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param wholeObjectSelection True if the {@link ComboBoxModel#getSelectedItem() getSelectedItem} should return the
	 *            whole object selected, false if it should return the value of the specified field for selected object
	 */
	public ComboBoxRenderModel(Class<T> objectType, boolean wholeObjectSelection) {
		this(objectType, new DefaultSelectionListDataSource<T>(objectType), wholeObjectSelection);
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param dataSource {@link DataSource Source} that will be used to provide data for the {@link DataModel model}
	 * @param wholeObjectSelection True if the {@link ComboBoxModel#getSelectedItem() getSelectedItem} should return the
	 *            whole object selected, false if it should return the value of the specified field for selected object
	 */
	public ComboBoxRenderModel(Class<T> objectType, SelectionListDataSource<T> dataSource, boolean wholeObjectSelection) {
		this(dataSource, new WholeObjectField<T>(objectType), wholeObjectSelection);
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param field {@link Field} that will be used to populate the list with data
	 * @param wholeObjectSelection True if the {@link ComboBoxModel#getSelectedItem() getSelectedItem} should return the
	 *            whole object selected, false if it should return the value of the specified field for selected object
	 */
	public ComboBoxRenderModel(Class<T> objectType, Field<T, ?> field, boolean wholeObjectSelection) {
		this(objectType, new DefaultDataModel<T>().addField(field), wholeObjectSelection);
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param dataModel {@link DataModel Model} that will be used to construct {@link DataRenderModel render model}
	 * @param wholeObjectSelection True if the {@link ComboBoxModel#getSelectedItem() getSelectedItem} should return the
	 *            whole object selected, false if it should return the value of the specified field for selected object
	 */
	public ComboBoxRenderModel(Class<T> objectType, DataModel<T> dataModel, boolean wholeObjectSelection) {
		this(new DefaultSelectionListDataSource<T>(objectType), dataModel, wholeObjectSelection);
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param dataSource {@link DataSource Source} that will be used to provide data for the {@link DataModel model}
	 * @param field {@link Field} that will be used to populate the list with data
	 * @param wholeObjectSelection True if the {@link ComboBoxModel#getSelectedItem() getSelectedItem} should return the
	 *            whole object selected, false if it should return the value of the specified field for selected object
	 */
	public ComboBoxRenderModel(SelectionListDataSource<T> dataSource, Field<T, ?> field, boolean wholeObjectSelection) {
		this(dataSource, new DefaultDataModel<T>().addField(field), wholeObjectSelection);
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param dataSource {@link DataSource Source} that will be used to provide data for the {@link DataModel model}
	 * @param dataModel {@link DataModel Model} that will be used to construct {@link DataRenderModel render model}
	 * @param wholeObjectSelection True if the {@link ComboBoxModel#getSelectedItem() getSelectedItem} should return the
	 *            whole object selected, false if it should return the value of the specified field for selected object
	 */
	public ComboBoxRenderModel(SelectionListDataSource<T> dataSource, DataModel<T> dataModel,
			boolean wholeObjectSelection) {
		this(new JComboBox(), dataSource, dataModel, wholeObjectSelection);
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param wholeObjectSelection True if the {@link ComboBoxModel#getSelectedItem() getSelectedItem} should return the
	 *            whole object selected, false if it should return the value of the specified field for selected object
	 */
	public ComboBoxRenderModel(Class<T> objectType, JComboBox comboBox, boolean wholeObjectSelection) {
		this(comboBox, new DefaultSelectionListDataSource<T>(objectType), wholeObjectSelection);
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param dataSource {@link DataSource Source} that will be used to provide data for the {@link DataModel model}
	 * @param wholeObjectSelection True if the {@link ComboBoxModel#getSelectedItem() getSelectedItem} should return the
	 *            whole object selected, false if it should return the value of the specified field for selected object
	 */
	public ComboBoxRenderModel(JComboBox comboBox, SelectionListDataSource<T> dataSource,
			boolean wholeObjectSelection) {
		this(comboBox, dataSource, new WholeObjectField<T>(dataSource.getElementType()), wholeObjectSelection);
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param field {@link Field} that will be used to populate the list with data
	 * @param wholeObjectSelection True if the {@link ComboBoxModel#getSelectedItem() getSelectedItem} should return the
	 *            whole object selected, false if it should return the value of the specified field for selected object
	 */
	public ComboBoxRenderModel(Class<T> objectType, JComboBox comboBox, Field<T, ?> field, boolean wholeObjectSelection) {
		this(objectType, comboBox, new DefaultDataModel<T>().addField(field), wholeObjectSelection);
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param dataModel {@link DataModel Model} that will be used to construct {@link DataRenderModel render model}
	 * @param wholeObjectSelection True if the {@link ComboBoxModel#getSelectedItem() getSelectedItem} should return the
	 *            whole object selected, false if it should return the value of the specified field for selected object
	 */
	public ComboBoxRenderModel(Class<T> objectType, JComboBox comboBox, DataModel<T> dataModel,
			boolean wholeObjectSelection) {
		this(comboBox, new DefaultSelectionListDataSource<T>(objectType), dataModel, wholeObjectSelection);
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param dataSource {@link DataSource Source} that will be used to provide data for the {@link DataModel model}
	 * @param field {@link Field} that will be used to populate the list with data
	 * @param wholeObjectSelection True if the {@link ComboBoxModel#getSelectedItem() getSelectedItem} should return the
	 *            whole object selected, false if it should return the value of the specified field for selected object
	 */
	public ComboBoxRenderModel(JComboBox comboBox, SelectionListDataSource<T> dataSource, Field<T, ?> field,
			boolean wholeObjectSelection) {
		this(comboBox, dataSource, new DefaultDataModel<T>().addField(field), wholeObjectSelection);
	}

	/**
	 * Constructs the {@link ComboBoxRenderModel}
	 * 
	 * @param comboBox {@link JComboBox} to be used for presenting data
	 * @param dataSource {@link DataSource Source} that will be used to provide data for the {@link DataModel model}
	 * @param dataModel {@link DataModel Model} that will be used to construct {@link DataRenderModel render model}
	 * @param wholeObjectSelection True if the {@link ComboBoxModel#getSelectedItem() getSelectedItem} should return the
	 *            whole object selected, false if it should return the value of the specified field for selected object
	 */
	public ComboBoxRenderModel(JComboBox comboBox, SelectionListDataSource<T> dataSource, DataModel<T> dataModel,
			boolean wholeObjectSelection) {
		if (comboBox == null || dataSource == null || dataModel == null)
			throw new IllegalArgumentException("None of the parameters cannot be null!");
		this.comboBox = comboBox;
		this.dataModel = dataModel;
		DataModel<T> comboDataModel = dataModel;
		if (wholeObjectSelection) {
			comboDataModel = new DefaultDataModel<T>();
			comboDataModel.addField(new WholeObjectField<T>(dataSource.getElementType()));
		}
		wholeObjectRenderer = new WholeObjectListRenderer(comboBox.getRenderer());
		comboModel = new CustomComboBoxModel<T>(comboDataModel, dataSource);
		comboBox.setModel(comboModel);
		if (wholeObjectSelection) {
			comboBox.setRenderer(wholeObjectRenderer);
		}

	}

	// ############## End Constructor Region ##############

	@Override
	public SelectionListDataSource<T> getDataSource() {
		return comboModel.getDataSource();
	}

	@Override
	public DataModel<T> getDataModel() {
		return dataModel;
		// return comboModel.getDataModel();
	}

	@Override
	public void setDataSource(SelectionListDataSource<T> source) {
		comboModel.setDataSource(source);
	}

	@Override
	public Component getRenderComponent() {
		return getComboBox();
	}

	/**
	 * @return The {@link JComboBox} used to show data
	 */
	public JComboBox getComboBox() {
		return comboBox;
	}

	public void setValues(List<T> values) {
		setValues(values, false);
	}

	public void setValues(List<T> values, boolean keepSelection) {
		T selectedValue = getDataSource().getSelectedValue();
		getDataSource().setData(values);
		if (keepSelection && selectedValue != null) {
			int index = getDataSource().indexOf(selectedValue);
			if (index == -1)
				throw new IllegalStateException(
						"Old selected value is not contained by the new list of values!");
			getDataSource().setSelectedIndices(index);
		}

	}

	private class WholeObjectListRenderer implements ListCellRenderer {
		private static final long serialVersionUID = 1L;
		private final ListCellRenderer renderer;

		public WholeObjectListRenderer(ListCellRenderer renderer) {
			this.renderer = renderer;
		}

		@Override
		@SuppressWarnings("unchecked")
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			if (renderer == null)
				return null;
			Component renderComponent = renderer.getListCellRendererComponent(list, value, index,
					isSelected, cellHasFocus);

			try {
				ReflectiveMethod<Component, Void> method =
						ReflectionUtils.getMethod(renderComponent.getClass(), "setText", String.class);
				T object = (T) value;
				Object fieldValue = getDataModel().getField(0).getValue(object);
				method.invoke(renderComponent, fieldValue == null ? "" : fieldValue.toString());
			} catch (Throwable e) {
			}

			return renderComponent;

			// NOTE:
			// Another method, but will work only for JLabel,
			// while previous one works with any component which has setText(text):

			// if (!(renderComponent instanceof JLabel))
			// return renderComponent;

			// JLabel label = (JLabel) renderComponent;
			// T object = (T) value;
			// Object fieldValue = getDataModel().getField(0).getValue(object);
			// label.setText(fieldValue == null ? "" : fieldValue.toString());
			// return label;
		}
	}
}