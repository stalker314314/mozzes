package org.mozzes.swing.mgf.component.table.rendering;

import java.util.HashMap;
import java.util.Map;

import javax.swing.table.TableCellRenderer;

public class DefaultCellRenderers {

	public static Map<Class<?>, TableCellRenderer> getDefaultRenderers() {
		Map<Class<?>, TableCellRenderer> map = new HashMap<Class<?>, TableCellRenderer>();

		// map.put(Date.class, new CustomDeteRenderer());

		return map;
	}
}
