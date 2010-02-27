/*
 * Copyright 2010 Mozzart
 *
 *
 * This file is part of mozzes.
 *
 * mozzes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mozzes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mozzes.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
