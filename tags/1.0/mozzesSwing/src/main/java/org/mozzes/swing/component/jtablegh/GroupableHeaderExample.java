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

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

/**
 * @version 1.0 11/09/98
 */
public class GroupableHeaderExample extends JFrame {

	private static final long serialVersionUID = -295740132686345456L;

	GroupableHeaderExample() {

		super("Groupable Header Example");
		DefaultTableModel dm = new DefaultTableModel();
		dm.setDataVector(new Object[][] { { "119", "foo", "bar", "ja", "ko", "zh" },
				{ "911", "bar", "foo", "en", "fr", "pt" } }, new Object[] { "SNo.", "1", "2", "Native", "2", "3" });

		JTable table = new JTable(dm) {
			private static final long serialVersionUID = -259442850855229523L;

			@Override
			protected JTableHeader createDefaultTableHeader() {
				return new GroupableTableHeader(columnModel);
			}
		};

		TableColumnModel cm = table.getColumnModel();
		ColumnGroup g_name = new ColumnGroup("Name");
		g_name.add(cm.getColumn(1));
		g_name.add(cm.getColumn(2));
		ColumnGroup g_lang = new ColumnGroup("Language");
		g_lang.add(cm.getColumn(3));
		ColumnGroup g_other = new ColumnGroup("Others");
		g_other.add(cm.getColumn(4));
		g_other.add(cm.getColumn(5));
		g_lang.add(g_other);

		GroupableTableHeader header = (GroupableTableHeader) table.getTableHeader();
		header.addColumnGroup(g_name);
		header.addColumnGroup(g_lang);
		JScrollPane scroll = new JScrollPane(table);
		getContentPane().add(scroll);
		setSize(400, 120);
	}

	public static void main(String[] args) {
		GroupableHeaderExample frame = new GroupableHeaderExample();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.setVisible(true);
	}
}
