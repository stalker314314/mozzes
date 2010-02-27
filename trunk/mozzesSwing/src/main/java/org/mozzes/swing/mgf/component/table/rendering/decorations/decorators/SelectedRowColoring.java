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
