package org.mozzes.swing.mgf.component.table.rendering.decorations.rules.value;

import org.mozzes.swing.mgf.component.table.rendering.decorations.CellDecorationRule;

public class Equals<T> extends CellDecorationRule {
	private final T object;

	public Equals(T object) {
		this.object = object;
	}

	@Override
	public boolean appliesTo(Context context) {
		Object value = context.getValue();
		if (value == null && object == null)
			return true;
		else if (value == null || object == null)
			return false;

		return object.equals(value);
	}
}
