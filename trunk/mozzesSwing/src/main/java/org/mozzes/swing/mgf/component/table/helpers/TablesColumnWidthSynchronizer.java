package org.mozzes.swing.mgf.component.table.helpers;

import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumnModel;

/**
 * Klasa koja omogucava sinhronizaciju promene sirine kolona u dve tabele
 * 
 * Ova klasa je TableColumnModelListener i ona se dodaje column modelu tabele na kojoj se vrsi promena sirine kolona. U
 * konstruktoru ove klase se dodaje tabela kojoj se sirina kolona menja na osnovu promena u prvoj tabeli. Cest slucaj
 * koriscenja je kod tabela koje imaju sumarni red koji se prikazuje kao posebna tabela ispod te tabele.
 * 
 * @author vaso
 */
public class TablesColumnWidthSynchronizer implements TableColumnModelListener {
	private JTable table;

	public TablesColumnWidthSynchronizer(JTable table) {
		this.table = table;
	}

	@Override
	public void columnMarginChanged(ChangeEvent e) {
		final TableColumnModel eventModel = (DefaultTableColumnModel) e.getSource();
		final TableColumnModel thisModel = table.getColumnModel();
		final int columnCount = eventModel.getColumnCount();

		for (int i = 0; i < columnCount; i++) {
			thisModel.getColumn(i).setPreferredWidth(eventModel.getColumn(i).getWidth());
		}
		table.repaint();
	}

	protected JTable getTable() {
		return table;
	}

	@Override
	public void columnAdded(TableColumnModelEvent e) {
	}

	@Override
	public void columnMoved(TableColumnModelEvent e) {
	}

	@Override
	public void columnRemoved(TableColumnModelEvent e) {
	}

	@Override
	public void columnSelectionChanged(ListSelectionEvent e) {
	}

	public static void setup(JTable mainTable, JTable synchronizedTable) {
		TablesColumnWidthSynchronizer synchronizer = new TablesColumnWidthSynchronizer(synchronizedTable);
		mainTable.getColumnModel().addColumnModelListener(synchronizer);
	}
}
