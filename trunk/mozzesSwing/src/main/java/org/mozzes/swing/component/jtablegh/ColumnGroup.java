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
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * ColumnGroup
 * 
 * @version 1.0 10/20/98
 * @author Nobuo Tamemasa
 */
public class ColumnGroup {

	protected TableCellRenderer renderer;

	protected Vector<Object> v;

	protected String text;

	protected int margin = 0;

	public ColumnGroup(String text) {
		this(null, text);
	}

	public ColumnGroup(TableCellRenderer renderer, String text) {
		if (renderer == null) {
			this.renderer = new DefaultTableCellRenderer() {

				private static final long serialVersionUID = 5561197754962810726L;

				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
						boolean hasFocus, int row, int column) {
					JTableHeader header = table.getTableHeader();
					if (header != null) {
						setForeground(header.getForeground());
						setBackground(header.getBackground());
						setFont(header.getFont());
					}
					setHorizontalAlignment(SwingConstants.CENTER);
					setText((value == null) ? "" : value.toString());
					// setBorder(UIManager.getDefaults().getBorder(("TableHeader.cellBorder")));
					setBorder(new LineBorder(Color.GRAY));
					return this;
				}
			};
		} else {
			this.renderer = renderer;
		}
		this.text = text;
		v = new Vector<Object>();
	}

	/**
	 * @param obj TableColumn or ColumnGroup
	 */
	public void add(Object obj) {
		if (obj == null) {
			return;
		}
		v.addElement(obj);
	}

	/**
	 * @param c TableColumn
	 * @param g ColumnGroups
	 */
	@SuppressWarnings("unchecked")
	public Vector<ColumnGroup> getColumnGroups(TableColumn c, Vector<ColumnGroup> g) {
		g.addElement(this);
		if (v.contains(c))
			return g;
		Enumeration<Object> e = v.elements();
		while (e.hasMoreElements()) {
			Object obj = e.nextElement();
			if (obj instanceof ColumnGroup) {
				Vector<ColumnGroup> groups = ((ColumnGroup) obj).getColumnGroups(c, (Vector<ColumnGroup>) g.clone());
				if (groups != null)
					return groups;
			}
		}
		return null;
	}

	public TableCellRenderer getHeaderRenderer() {
		return renderer;
	}

	public void setHeaderRenderer(TableCellRenderer renderer) {
		if (renderer != null) {
			this.renderer = renderer;
		}
	}

	public Object getHeaderValue() {
		return text;
	}

	public Dimension getSize(JTable table) {
		Component comp = renderer.getTableCellRendererComponent(table, getHeaderValue(), false, false, -1, -1);
		int height = comp.getPreferredSize().height;
		int width = 0;
		Enumeration<Object> e = v.elements();
		while (e.hasMoreElements()) {
			Object obj = e.nextElement();
			if (obj instanceof TableColumn) {
				TableColumn aColumn = (TableColumn) obj;
				width += aColumn.getWidth();
				width += margin;
			} else {
				width += ((ColumnGroup) obj).getSize(table).width;
			}
		}
		return new Dimension(width, height);
	}

	public void setColumnMargin(int margin) {
		this.margin = margin;
		Enumeration<Object> e = v.elements();
		while (e.hasMoreElements()) {
			Object obj = e.nextElement();
			if (obj instanceof ColumnGroup) {
				((ColumnGroup) obj).setColumnMargin(margin);
			}
		}
	}
}
