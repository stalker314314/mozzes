package org.mozzes.swing.mgf.component.table.rendering.decorations.rules.rows;

import org.mozzes.swing.mgf.component.table.rendering.decorations.CellDecorationRule;

public class RowNumberEquals extends CellDecorationRule {
	private final int row;

	public RowNumberEquals(int row) {
		this.row = row;
	}

	@Override
	public boolean appliesTo(Context context) {
		return context.getRow() == row;
	}

}
