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