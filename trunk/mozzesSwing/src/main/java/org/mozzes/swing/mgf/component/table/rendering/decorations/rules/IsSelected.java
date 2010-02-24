package org.mozzes.swing.mgf.component.table.rendering.decorations.rules;

import org.mozzes.swing.mgf.component.table.rendering.decorations.CellDecorationRule;

public class IsSelected extends CellDecorationRule {

	@Override
	public boolean appliesTo(Context context) {
		return context.isSelected();
	}

}
