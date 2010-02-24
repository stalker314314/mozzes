package org.mozzes.swing.mgf.component.table.rendering.decorations.rules.rows;

import org.mozzes.swing.mgf.component.table.rendering.decorations.CellDecorationRule;

public class LastRow extends CellDecorationRule {

	@Override
	public boolean appliesTo(Context context) {
		return context.getRow() == context.getTable().getModel().getRowCount() - 1;
	}

}
