package org.mozzes.swing.mgf.component.table.rendering.decorations.decorators;

import java.awt.Color;

import javax.swing.UIManager;
import javax.swing.border.Border;

import org.mozzes.swing.mgf.component.table.rendering.decorations.RuleBasedCellDecorator;
import org.mozzes.swing.mgf.component.table.rendering.decorations.CellDecorationRule.Context;
import org.mozzes.swing.mgf.component.table.rendering.decorations.rules.IsSelected;
import org.mozzes.swing.mgf.component.table.rendering.decorations.rules.cell.HasFocus;
import org.mozzes.swing.mgf.component.table.rendering.decorations.rules.cell.IsEditable;
import org.mozzes.swing.mgf.decoration.decorators.BackgroundDecorator;
import org.mozzes.swing.mgf.decoration.decorators.BorderDecorator;
import org.mozzes.swing.mgf.decoration.decorators.ForegroundDecorator;
import org.mozzes.utils.rulesengine.rules.IsNull;


public class CellFocusBorder extends RuleBasedCellDecorator {

	private final Border selectedFocused;
	private final Border focusedOnly;
	private final Color notSelectedEditableBackground;
	private final Color notSelectedEditableForeground;

	public CellFocusBorder() {
		this((Border) UIManager.getDefaults().get("Table.focusSelectedCellHighlightBorder"),
				(Border) UIManager.getDefaults().get("Table.focusCellHighlightBorder"),
				UIManager.getColor("Table.focusCellBackground"),
				UIManager.getColor("Table.focusCellForeground"));
	}

	public CellFocusBorder(Border selectedFocused, Border focusedOnly,
			Color notSelectedEditableBackground, Color notSelectedEditableForeground) {
		super(new HasFocus());

		this.selectedFocused = selectedFocused;
		this.focusedOnly = focusedOnly;
		this.notSelectedEditableBackground = notSelectedEditableBackground;
		this.notSelectedEditableForeground = notSelectedEditableForeground;
		addRules();
	}

	private void addRules() {
		add(new IsSelected().and(new IsNull<Context>(selectedFocused).inverse()),
				new BorderDecorator(selectedFocused, true));

		add(new IsNull<Context>(selectedFocused),
				new BorderDecorator(focusedOnly, true));

		add(new IsSelected().inverse().and(new IsEditable())
				.and(new IsNull<Context>(notSelectedEditableBackground).inverse()),
				new BackgroundDecorator(notSelectedEditableBackground));

		add(new IsSelected().inverse().and(new IsEditable())
				.and(new IsNull<Context>(notSelectedEditableForeground).inverse()),
				new ForegroundDecorator(notSelectedEditableForeground));
	}
}
