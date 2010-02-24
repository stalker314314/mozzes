package org.mozzes.swing.mgf.component.table.rendering.decorations.decorators;

import java.awt.Color;

import javax.swing.JTable;

import org.mozzes.swing.mgf.component.table.rendering.decorations.CellDecorationRule;
import org.mozzes.swing.mgf.component.table.rendering.decorations.RuleBasedCellDecorator;
import org.mozzes.swing.mgf.component.table.rendering.decorations.rules.IsSelected;
import org.mozzes.swing.mgf.decoration.decorators.BackgroundDecorator;
import org.mozzes.swing.mgf.decoration.decorators.ForegroundDecorator;


public class SelectedRowColoring extends RuleBasedCellDecorator {
	private final Color background;
	private final Color foreground;

	public SelectedRowColoring(JTable table) {
		this(table.getSelectionBackground(), table.getSelectionForeground());
	}

	public SelectedRowColoring(Color background, Color foreground) {
		// super(new IsSelected().or(new HasFocus()));
		super(new IsSelected());

		this.background = background;
		this.foreground = foreground;
		addRules();
	}

	private void addRules() {
		add(new BackgroundDecorator(background));
		// add(new ForegroundDecorator(foreground));

		// add(new BackgroundEquals().inverse(), new BackgroundDecorator(background));
		add(new ForegroundEquals().inverse(), new ForegroundDecorator(foreground));
	}

	@SuppressWarnings("unused")
	private class BackgroundEquals extends CellDecorationRule {
		@Override
		public boolean appliesTo(Context context) {
			return background.equals(context.getComponent().getBackground());
		}
	}

	private class ForegroundEquals extends CellDecorationRule {
		@Override
		public boolean appliesTo(Context context) {
			return foreground.equals(context.getComponent().getForeground());
		}
	}
}
