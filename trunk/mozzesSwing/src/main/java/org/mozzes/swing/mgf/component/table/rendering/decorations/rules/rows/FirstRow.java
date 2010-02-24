package org.mozzes.swing.mgf.component.table.rendering.decorations.rules.rows;

import org.mozzes.swing.mgf.component.table.rendering.decorations.CellDecorationRule;

public class FirstRow extends CellDecorationRule {

	@Override
	public boolean appliesTo(Context context) {
		return context.getRow() == 0;
	}

}
