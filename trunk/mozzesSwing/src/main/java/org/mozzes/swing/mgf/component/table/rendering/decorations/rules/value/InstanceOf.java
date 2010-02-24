package org.mozzes.swing.mgf.component.table.rendering.decorations.rules.value;

import org.mozzes.swing.mgf.component.table.rendering.decorations.CellDecorationRule;

public class InstanceOf extends CellDecorationRule {
	private final Class<?> clazz;

	public InstanceOf(Class<?> clazz) {
		this.clazz = clazz;
	}

	@Override
	public boolean appliesTo(Context context) {
		Object value = context.getValue();
		Class<?> valueClazz;
		if (value == null)
			valueClazz = context.getTable().getModel().getColumnClass(context.getColumn());
		else
			valueClazz = value.getClass();
		return clazz.isAssignableFrom(valueClazz);
	}
}
