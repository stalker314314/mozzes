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
