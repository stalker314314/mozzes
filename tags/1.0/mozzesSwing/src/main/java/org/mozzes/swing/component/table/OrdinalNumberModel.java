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
package org.mozzes.swing.component.table;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * {@link TableModel} objekat za tabelu koja prikazuje redni broj redova
 * @author Borko Grecic
 */
public class OrdinalNumberModel<R extends TableModel> extends AbstractTableModel implements TableModelListener {

	/**
	 * Osnovni {@link TableModel} objekat
	 */
	private final R baseModel;
	
	/**
	 * Naziv kolone koja prikazuje redne brojeve
	 */
	private final String columnName;
	
	/**
	 * Konstruktor {@link OrdinalNumberModel} objekta
	 * @param model Osnovni {@link TableModel} objekat
	 */
	public OrdinalNumberModel(final R model) {
		this(model, "#");
	}
	
	/**
	 * Konstruktor {@link OrdinalNumberModel} objekta
	 * @param model Osnovni {@link TableModel} objekat
	 * @param columnName Naziv kolone koja prikazuje redne brojeve
	 */
	public OrdinalNumberModel(final R model, final String columnName) {
		this.baseModel = model;
		this.columnName = columnName;
		
		baseModel.addTableModelListener(this);
	}
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 257693883230884747L;

	/*
	 * (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return this.baseModel.getRowCount();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return Integer.valueOf(rowIndex + 1);
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return Integer.class;
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) {
		return this.columnName;
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
	 */
	@Override
	public void tableChanged(TableModelEvent e) {
		fireTableChanged(e);
	}
}