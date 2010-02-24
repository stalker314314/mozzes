package org.mozzes.swing.mgf.component.table.rendering.decorations.rules.cell;

import org.mozzes.swing.mgf.component.table.rendering.decorations.CellDecorationRule;

public class HasFocus extends CellDecorationRule {

	@Override
	public boolean appliesTo(Context context) {
		return context.isHasFocus();
	}

}
