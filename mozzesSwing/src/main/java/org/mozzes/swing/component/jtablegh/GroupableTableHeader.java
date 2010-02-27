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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * GroupableTableHeader
 * 
 * @version 1.0 10/20/98
 * @author Nobuo Tamemasa
 */
public class GroupableTableHeader extends JTableHeader {

	private static final long serialVersionUID = -6129621895719129733L;

	protected Vector<ColumnGroup> columnGroups = null;

	public GroupableTableHeader(TableColumnModel model) {
		super(model);
		setUI(new GroupableTableHeaderUI());
		setReorderingAllowed(false);
	}

	@Override
	public void updateUI() {
		setUI(new GroupableTableHeaderUI());
	}

	@Override
	public void setReorderingAllowed(boolean b) {
		reorderingAllowed = false;
	}

	public void addColumnGroup(ColumnGroup g) {
		if (columnGroups == null) {
			columnGroups = new Vector<ColumnGroup>();
		}
		columnGroups.addElement(g);
	}

	public Enumeration<ColumnGroup> getColumnGroups(TableColumn col) {
		if (columnGroups == null)
			return null;
		Enumeration<ColumnGroup> e = columnGroups.elements();
		while (e.hasMoreElements()) {
			ColumnGroup cGroup = e.nextElement();
			Vector<ColumnGroup> v_ret = cGroup.getColumnGroups(col, new Vector<ColumnGroup>());
			if (v_ret != null) {
				return v_ret.elements();
			}
		}
		return null;
	}

	public void setColumnMargin() {
		if (columnGroups == null)
			return;
		int columnMargin = getColumnModel().getColumnMargin();
		Enumeration<ColumnGroup> e = columnGroups.elements();
		while (e.hasMoreElements()) {
			ColumnGroup cGroup = e.nextElement();
			cGroup.setColumnMargin(columnMargin);
		}
	}

	private static class GroupableTableHeaderUI extends BasicTableHeaderUI {

		@Override
		public void paint(Graphics g, JComponent c) {
			Rectangle clipBounds = g.getClipBounds();
			if (header.getColumnModel() == null)
				return;
			((GroupableTableHeader) header).setColumnMargin();
			int column = 0;
			Dimension size = header.getSize();
			Rectangle cellRect = new Rectangle(0, 0, size.width, size.height);
			Hashtable<ColumnGroup, Rectangle> h = new Hashtable<ColumnGroup, Rectangle>();
			int columnMargin = header.getColumnModel().getColumnMargin();

			Enumeration<TableColumn> enumeration = header.getColumnModel().getColumns();
			while (enumeration.hasMoreElements()) {
				cellRect.height = size.height;
				cellRect.y = 0;
				TableColumn aColumn = enumeration.nextElement();
				Enumeration<ColumnGroup> cGroups = ((GroupableTableHeader) header).getColumnGroups(aColumn);

				if (cGroups != null) {
					int groupHeight = 0;
					while (cGroups.hasMoreElements()) {
						ColumnGroup cGroup = cGroups.nextElement();
						Rectangle groupRect = h.get(cGroup);
						if (groupRect == null) {
							groupRect = new Rectangle(cellRect);
							Dimension d = cGroup.getSize(header.getTable());
							groupRect.width = d.width;
							groupRect.height = d.height;
							h.put(cGroup, groupRect);
						}
						paintCell(g, groupRect, cGroup);
						groupHeight += groupRect.height;
						cellRect.height = size.height - groupHeight;
						cellRect.y = groupHeight;
					}
				}
				cellRect.width = aColumn.getWidth() + columnMargin;
				if (cellRect.intersects(clipBounds)) {
					paintCell(g, cellRect, column);
				}
				cellRect.x += cellRect.width;
				column++;
			}
		}

		private void paintCell(Graphics g, Rectangle cellRect, int columnIndex) {
			TableColumn aColumn = header.getColumnModel().getColumn(columnIndex);
			TableCellRenderer renderer = aColumn.getHeaderRenderer();
			// revised by Java2s.com
			renderer = new DefaultTableCellRenderer() {
				private static final long serialVersionUID = 8443701778447713410L;

				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
						boolean hasFocus, int row, int column) {
					JLabel header = new JLabel();
					header.setForeground(table.getTableHeader().getForeground());
					header.setBackground(table.getTableHeader().getBackground());
					header.setFont(table.getTableHeader().getFont());

					header.setHorizontalAlignment(SwingConstants.CENTER);
					header.setText(value.toString());
					// header.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
					header.setBorder(new LineBorder(Color.GRAY));
					return header;
				}

			};
			Component c = renderer.getTableCellRendererComponent(header.getTable(), aColumn.getHeaderValue(), false,
					false, -1, columnIndex);

			c.setBackground(UIManager.getColor("control"));

			rendererPane.add(c);
			rendererPane.paintComponent(g, c, header, cellRect.x, cellRect.y, cellRect.width, cellRect.height, true);
		}

		private void paintCell(Graphics g, Rectangle cellRect, ColumnGroup cGroup) {
			TableCellRenderer renderer = cGroup.getHeaderRenderer();
			// revised by Java2s.com
			// if(renderer == null){
			// return ;
			// }

			Component component = renderer.getTableCellRendererComponent(header.getTable(), cGroup.getHeaderValue(),
					false, false, -1, -1);
			rendererPane.add(component);
			rendererPane.paintComponent(g, component, header, cellRect.x, cellRect.y, cellRect.width, cellRect.height,
					true);
		}

		private int getHeaderHeight() {
			int height = 0;
			TableColumnModel columnModel = header.getColumnModel();
			for (int column = 0; column < columnModel.getColumnCount(); column++) {
				TableColumn aColumn = columnModel.getColumn(column);
				TableCellRenderer renderer = aColumn.getHeaderRenderer();
				// revised by Java2s.com
				if (renderer == null) {
					return 60;
				}

				Component comp = renderer.getTableCellRendererComponent(header.getTable(), aColumn.getHeaderValue(),
						false, false, -1, column);
				int cHeight = comp.getPreferredSize().height;
				Enumeration<ColumnGroup> e = ((GroupableTableHeader) header).getColumnGroups(aColumn);
				if (e != null) {
					while (e.hasMoreElements()) {
						ColumnGroup cGroup = e.nextElement();
						cHeight += cGroup.getSize(header.getTable()).height;
					}
				}
				height = Math.max(height, cHeight);
			}
			return height;
		}

		private Dimension createHeaderSize(long aWidth) {
			long width = aWidth;
			TableColumnModel columnModel = header.getColumnModel();
			width += columnModel.getColumnMargin() * columnModel.getColumnCount();
			if (width > Integer.MAX_VALUE) {
				width = Integer.MAX_VALUE;
			}
			return new Dimension((int) width, getHeaderHeight());
		}

		@Override
		public Dimension getPreferredSize(JComponent c) {
			long width = 0;
			Enumeration<TableColumn> enumeration = header.getColumnModel().getColumns();
			while (enumeration.hasMoreElements()) {
				TableColumn aColumn = enumeration.nextElement();
				width = width + aColumn.getPreferredWidth();
			}
			return createHeaderSize(width);
		}
	}
}
