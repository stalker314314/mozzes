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
package org.mozzes.swing.mgf.component.table.rendering.renderers;

import java.awt.Component;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.UIDefaults;
import javax.swing.table.TableCellRenderer;

public class DefaultCellRenderer implements TableCellRenderer {
	private UIDefaults defaults;

	public DefaultCellRenderer() {
		initDefaultRenderers();
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Component component;

		Class<?> valueClass = value == null ?
				table.getModel().getColumnClass(column) :
				value.getClass();

		component = getDefaultRendererFor(valueClass).getTableCellRendererComponent(
				table, value, isSelected, hasFocus, row, column);

		if (component instanceof JComponent)
			((JComponent) component).setOpaque(true);
		return component;
	}

	public TableCellRenderer getDefaultRendererFor(Class<?> valueClass) {
		if (valueClass == null) {
			return null;
		} else {
			Object renderer = defaults.get(valueClass);
			if (renderer != null) {
				return (TableCellRenderer) renderer;
			} else {
				return getDefaultRendererFor(valueClass.getSuperclass());
			}
		}
	}

	private void initDefaultRenderers() {
		if (defaults != null)
			return;
		defaults = new UIDefaults(8, 0.75f);

		// Objects
		defaults.put(Object.class,
				new UIDefaults.ProxyLazyValue("javax.swing.table.DefaultTableCellRenderer$UIResource"));

		// Numbers
		defaults.put(Number.class, new UIDefaults.ProxyLazyValue("javax.swing.JTable$NumberRenderer"));

		// Doubles and Floats
		defaults.put(Float.class, new UIDefaults.ProxyLazyValue("javax.swing.JTable$DoubleRenderer"));
		defaults.put(Double.class, new UIDefaults.ProxyLazyValue("javax.swing.JTable$DoubleRenderer"));

		// Dates
		defaults.put(Date.class, new UIDefaults.ProxyLazyValue("javax.swing.JTable$DateRenderer"));

		// Icons and ImageIcons
		defaults.put(Icon.class, new UIDefaults.ProxyLazyValue("javax.swing.JTable$IconRenderer"));
		defaults.put(ImageIcon.class, new UIDefaults.ProxyLazyValue("javax.swing.JTable$IconRenderer"));

		// Booleans
		defaults.put(Boolean.class, new UIDefaults.ProxyLazyValue("javax.swing.JTable$BooleanRenderer"));
	}
}
