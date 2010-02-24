package org.mozzes.swing.mgf.component.table.rendering.decorations.rules.column;

import org.mozzes.swing.mgf.component.table.rendering.decorations.CellDecorationRule.Context;
import org.mozzes.utils.rulesengine.Rule;


public class ColumnNumberEquals extends Rule<Context> {

	private final int column;

	public ColumnNumberEquals(int column) {
		this.column = column;
	}

	@Override
	public boolean appliesTo(Context context) {
		return context.getColumn() == column;
	}

}
