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

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * This extends {@link JTable} by adding a row number column to it in a proper manner
 * 
 * @author milos
 */
class RowNumberTable extends JTable {
	private static final long serialVersionUID = 15L;
	public static int DEFAULT_ROW_NUMBER_COLUMN_WIDTH = 50;
	private final JTable mainTable;
	private TableRowSorter<TableModel> sorter;

	/**
	 * Constructs {@link RowNumberTable} for the given {@link JTable}
	 * 
	 * @param table {@link JTable} to be wrapped in a {@link RowNumberTable}
	 */
	private RowNumberTable(JTable table) {
		super();
		mainTable = table;
		setAutoCreateColumnsFromModel(false);
		setModel(mainTable.getModel());
		setupRowSorter();
		setSelectionModel(mainTable.getSelectionModel());
		setColumnSelectionAllowed(false);
		setCellSelectionEnabled(false);
		setRowSelectionAllowed(false);
		setFocusable(false);
		setAutoscrolls(false);
		((JLabel) getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		addColumn(new TableColumn());
		setWidth(DEFAULT_ROW_NUMBER_COLUMN_WIDTH);
	}

	@SuppressWarnings("unchecked")
	private void setupRowSorter() {
		sorter = new TableRowSorter<TableModel>(mainTable.getModel());
		setRowSorter(sorter);
		final TableRowSorter<TableModel> rowSorter = (TableRowSorter<TableModel>) mainTable.getRowSorter();
		rowSorter.addRowSorterListener(new RowSorterListener() {
			@Override
			public void sorterChanged(RowSorterEvent e) {
				sorter.setRowFilter(rowSorter.getRowFilter());
			}
		});
	}

	@Override
	public int getWidth() {
		return getColumnModel().getColumn(0).getPreferredWidth();
	}

	public void setWidth(int width) {
		getColumnModel().getColumn(0).setCellRenderer(getTableHeader().getDefaultRenderer());
		getColumnModel().getColumn(0).setPreferredWidth(width);
		setPreferredScrollableViewportSize(getPreferredSize());
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	@Override
	public Object getValueAt(int row, int column) {
		return Integer.valueOf(row + 1);
	}

	@Override
	public int getRowHeight(int row) {
		return mainTable.getRowHeight();
	}

	/**
	 * Creates and sets up a {@link JScrollPane} containing the {@link RowNumberTable} for the specified {@link JTable}
	 * 
	 * @param table {@link JTable} for which the row number table shall be constructed
	 * @return {@link JScrollPane} containing the created {@link RowNumberTable}
	 */
	public static ScrollPaneWithRowHeader createScrollPane(JTable table) {
		RowNumberTable rowNumberTable = new RowNumberTable(table);
		ScrollPaneWithRowHeader scrollPane = new ScrollPaneWithRowHeader(table, rowNumberTable);
		scrollPane.setRowHeaderView(rowNumberTable);
		scrollPane.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, new CornerTable(table));
		return scrollPane;
	}

	/**
	 * Just for an eye candy, this table is set it the top left corner of {@link JScrollPane} because it looks better
	 * that way
	 * 
	 * @author milos
	 */
	private static class CornerTable extends JTable {
		private static final long serialVersionUID = 15L;

		private final JTableHeader header;

		/**
		 * Constructs {@link CornerTable} for the given {@link JTable}
		 * 
		 * @param table {@link JTable} to be wrapped in a {@link RowNumberTable}
		 */
		public CornerTable(JTable table) {
			super(new DefaultTableModel(1, 1));
			this.header = table.getTableHeader();
			setAutoscrolls(false);
			setEnabled(false);
			getColumnModel().getColumn(0).setCellRenderer(header.getDefaultRenderer());
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}

		@Override
		public Object getValueAt(int row, int column) {
			return null;
		}

		@Override
		public int getRowHeight(int row) {
			return header.getHeight();
		}
	}
}