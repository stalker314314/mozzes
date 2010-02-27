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

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.mozzes.swing.mgf.component.table.editing.CellEditingFacility;
import org.mozzes.swing.mgf.component.table.rendering.CellRenderingFacility;
import org.mozzes.swing.mgf.datamodel.DataModel;
import org.mozzes.swing.mgf.datarenderer.table.filter.FilterComponentBuilder;
import org.mozzes.swing.mgf.datarenderer.table.filter.FilteringFacility;
import org.mozzes.swing.mgf.datarenderer.table.sumrow.SumRowBuilder;
import org.mozzes.swing.mgf.datasource.SelectionListDataSource;
import org.mozzes.swing.mgf.helpers.datasource.BeanToListDataSource;


public class TableBuilder<T> {
	private SelectionListDataSource<T> source;
	private final DataModel<T> model;

	private TableRenderModel<T> tableRenderModel;
	private SumRowBuilder<T> sumarumTableBuilder;
	private FilterComponentBuilder<T> filterBuilder;

	private boolean showRowNumber = true;
	private boolean showSumarumTable = false;
	private boolean showFilterComponent = false;

	/**
	 * Start building table
	 * 
	 * @param source Source which will populate the table
	 * @param model Model which defines table columns
	 */
	public TableBuilder(SelectionListDataSource<T> source, DataModel<T> model) {
		this.source = source;
		this.model = model;
	}

	/**
	 * Sets up the specified table for use with passed source and model
	 * 
	 * @param <T> Type of a bean contained by one row of the table
	 * @param table Table to be set up
	 * @param source Source which will populate the table
	 * @param model Model which defines table columns
	 */
	public static <T> void setupTable(JTable table, SelectionListDataSource<T> source, DataModel<T> model) {
		new TableRenderModel<T>(table, source, model, false);
	}

	/**
	 * Replaces source which populates the table
	 * 
	 * @param source Source which will populate the table
	 */
	public TableBuilder<T> setSource(SelectionListDataSource<T> source) {
		this.source = source;
		if (getRenderModel() != null)
			getRenderModel().setDataSource(source);
		if (getSumRowBuilder() != null)
			getSumRowBuilder().getRenderModel().setDataSource(
					new BeanToListDataSource<List<T>>(source));

		return this;
	}

	/**
	 * @return Source which populates the table
	 */
	public SelectionListDataSource<T> getSource() {
		return source;
	}

	/**
	 * @return Model which defines table columns
	 */
	public DataModel<T> getModel() {
		return model;
	}

	/**
	 * Sets the width of row number column (and padding of the sum row if it exists)
	 */
	public TableBuilder<T> setRowNumberColumnWidth(int width) {
		getRenderModel().setRowNumberColumnWidth(width);
		if (getSumRowBuilder() != null && getRenderModel().isRowNumberColumnVisible())
			getSumRowBuilder().setPadding(width);
		return this;
	}

	/**
	 * @return {@link TableRenderModel#getCellRenderingFacility()}
	 */
	public CellRenderingFacility getCellRenderingFacility() {
		return getRenderModel().getCellRenderingFacility();
	}

	/**
	 * @return {@link TableRenderModel#getCellEditingFacility()}
	 */
	public CellEditingFacility getCellEditingFacility() {
		return getRenderModel().getCellEditingFacility();
	}

	/**
	 * @return {@link TableRenderModel#getFilteringFacility()}
	 */
	public FilteringFacility<T> getFilteringFacility() {
		return getRenderModel().getFilteringFacility();
	}

	/**
	 * @return {@link CellRenderingFacility} of a sum row table
	 */
	public CellRenderingFacility getSumRowCellRenderingFacility() {
		checkHasSumRow();
		return getSumRowBuilder().getRenderModel().getCellRenderingFacility();
	}

	public TableBuilder<T> setShowRowNumber(boolean showRownumber) {
		this.showRowNumber = showRownumber;
		return this;
	}

	public TableBuilder<T> setShowSumRow(boolean showSumarumTable) {
		this.showSumarumTable = showSumarumTable;
		return this;
	}

	public TableBuilder<T> setShowFilterComponent(boolean showFilter) {
		this.showFilterComponent = showFilter;
		return this;
	}

	public boolean isShowRowNumber() {
		return showRowNumber;
	}

	public boolean isShowSumarumTable() {
		return showSumarumTable;
	}

	public boolean isShowFilterComponent() {
		return showFilterComponent;
	}

	/**
	 * @return {@link SumRowBuilder} for sum row configuration
	 */
	public SumRowBuilder<T> getSumRowBuilder() {
		if (source == null || model == null)
			throw new IllegalArgumentException("Source and model must not be null!");
		if (!showSumarumTable)
			return null;
		if (sumarumTableBuilder == null && showSumarumTable)
			sumarumTableBuilder = new SumRowBuilder<T>(getRenderModel());
		return sumarumTableBuilder;
	}

	/**
	 * @return {@link FilterComponentBuilder} for filter component configuration
	 */
	public FilterComponentBuilder<T> getFilterComponentBuilder() {
		if (source == null || model == null)
			throw new IllegalArgumentException("Source and model must not be null!");
		if (!showFilterComponent)
			return null;
		if (filterBuilder == null && showFilterComponent)
			filterBuilder = new FilterComponentBuilder<T>(getRenderModel());

		return filterBuilder;
	}

	/**
	 * @return {@link TableRenderModel} of the main table
	 */
	public TableRenderModel<T> getRenderModel() {
		if (source == null || model == null)
			throw new IllegalArgumentException("Source and model must not be null!");
		if (tableRenderModel == null)
			tableRenderModel = new TableRenderModel<T>(source, model, showRowNumber);
		return tableRenderModel;
	}

	/**
	 * @return {@link TableRenderModel} of the table which contains sum row
	 */
	public TableRenderModel<List<T>> getSumRowRenderModel() {
		checkHasSumRow();
		return getSumRowBuilder().getRenderModel();
	}

	/**
	 * @return Main table
	 */
	public JTable getTable() {
		return getRenderModel().getTable();
	}

	/**
	 * @return Table which contains sum row
	 */
	public JTable getSumRow() {
		checkHasSumRow();
		return getSumRowBuilder().getTable();
	}

	/**
	 * @return {@link JScrollPane} of the main table
	 */
	public JScrollPane getScrollPane() {
		return getRenderModel().getScrollPane();
	}

	/**
	 * @return {@link JPanel} with all appropriate components
	 */
	public Component getRenderComponent() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(getRenderModel().getRenderComponent(), BorderLayout.CENTER);
		if (showSumarumTable) {
			panel.add(getSumRowBuilder().getRenderComponent(), BorderLayout.SOUTH);
		}
		if (showFilterComponent) {
			panel.add(getFilterComponentBuilder().getRenderComponent(), BorderLayout.NORTH);
		}

		return panel;
	}

	/**
	 * Throws exception if sum row does not exist
	 */
	private void checkHasSumRow() {
		if (getSumRowBuilder() == null)
			throw new IllegalStateException("Table does not have a sum row!");
	}

}
