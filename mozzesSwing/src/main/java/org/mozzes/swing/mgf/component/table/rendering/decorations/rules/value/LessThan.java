package org.mozzes.swing.mgf.component.table.rendering.decorations.rules.value;

import org.mozzes.swing.mgf.component.table.rendering.decorations.CellDecorationRule;

public class LessThan<T> extends CellDecorationRule {
	private final Comparable<T> than;
	private final InstanceOf valueClass;

	public LessThan(Comparable<T> than) {
		if (than == null)
			throw new IllegalArgumentException("Comparsion value cannot be null!");
		this.than = than;
		valueClass = new InstanceOf(than.getClass());
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean appliesTo(Context context) {
		Object value = context.getValue();
		if (value == null)
			return false;
		return valueClass.appliesTo(context) && than.compareTo((T) value) > 0;
	}
}
