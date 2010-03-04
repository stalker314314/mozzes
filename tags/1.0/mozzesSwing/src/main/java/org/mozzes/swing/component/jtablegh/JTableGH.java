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
