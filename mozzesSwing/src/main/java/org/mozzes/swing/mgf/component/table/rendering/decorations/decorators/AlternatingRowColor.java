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

import javax.swing.UIManager;

import org.mozzes.swing.mgf.component.table.rendering.decorations.RuleBasedCellDecorator;
import org.mozzes.swing.mgf.component.table.rendering.decorations.rules.IsSelected;
import org.mozzes.swing.mgf.component.table.rendering.decorations.rules.rows.EvenRow;
import org.mozzes.swing.mgf.decoration.decorators.BackgroundDecorator;


public class AlternatingRowColor extends RuleBasedCellDecorator {
	private final Color even;
	private final Color odd;

	public AlternatingRowColor() {
		this(UIManager.getColor("Table.alternateRowColor"), Color.WHITE);
	}

	public AlternatingRowColor(Color even, Color odd) {
		super(new IsSelected().inverse());
		this.even = even;
		this.odd = odd;
		addRules();
	}

	private void addRules() {
		add(new EvenRow(), new BackgroundDecorator(even));
		add(new EvenRow().inverse(), new BackgroundDecorator(odd));
	}
}
