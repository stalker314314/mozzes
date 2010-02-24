package org.mozzes.swing.component.jtablegh;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;

/**
 * JTable koja radi sa grupabilnim header-ima. Uz nju se koriste klase {@link GroupableTableHeader}, i
 * {@link ColumnGroup}.
 * 
 * @author srdjan
 */
public class JTableGH extends JTable {

	private static final long serialVersionUID = -259442850855229523L;

	@Override
	protected JTableHeader createDefaultTableHeader() {
		return new GroupableTableHeader(columnModel);
	}

	/**
	 * Kreira novu header grupu, nad ovom tabelom, koja unutar sebe vezuje kolone koje su zadate indexima. Ova metoda se
	 * mora pozvati posle kreiranja obicnih kolona u tabeli.
	 */
	public void createGroupHeader(String groupName, Integer... columnIndexes) {

		GroupableTableHeader header = (GroupableTableHeader) this.getTableHeader();
		ColumnGroup group = new ColumnGroup(groupName);

		for (Integer index : columnIndexes) {
			group.add(this.getColumnModel().getColumn(index));
		}

		header.addColumnGroup(group);
	}
}
