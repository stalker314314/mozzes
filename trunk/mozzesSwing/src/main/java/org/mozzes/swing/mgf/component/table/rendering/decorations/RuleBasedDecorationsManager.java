package org.mozzes.swing.mgf.component.table.rendering.decorations;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.mozzes.swing.mgf.component.table.rendering.decorations.CellDecorationRule.Context;
import org.mozzes.swing.mgf.decoration.DecorationManager;
import org.mozzes.swing.mgf.decoration.Decorator;


public class RuleBasedDecorationsManager {
	private final List<RuleBasedCellDecorator> decorators = new ArrayList<RuleBasedCellDecorator>();
	private DecorationManager decorationManager = DecorationManager.getInstance();

	public void applyDecoration(Context context, JComponent component) {
		for (RuleBasedCellDecorator decorator : decorators) {
			List<Decorator> decorations = decorator.getDecorations(context);
			if (decorations == null)
				continue;
			for (Decorator decoration : decorations) {
				decoration.decorate(component);
		}
	}
	}

	public void add(RuleBasedCellDecorator decorator) {
		decorators.add(decorator);
	}

	public void clearRuleDecorations(JComponent component, Context context) {
		for (RuleBasedCellDecorator decorator : decorators) {
			List<Decorator> decorations = decorator.getDecorations(context);
			if (decorations == null)
				continue;
			for (Decorator decoration : decorations) {
				decoration.clear(component);
	}
		}
	}

	public void clearAllDecorations(JComponent component) {
		decorationManager.clearAllDecorations(component);
	}
}
