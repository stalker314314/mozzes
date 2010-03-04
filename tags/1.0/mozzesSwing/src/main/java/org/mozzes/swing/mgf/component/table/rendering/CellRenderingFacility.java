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
package org.mozzes.swing.mgf.component.table.rendering;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.CellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import org.mozzes.swing.mgf.component.table.rendering.decorations.RuleBasedCellDecorator;
import org.mozzes.swing.mgf.component.table.rendering.decorations.CellDecorationRule.Context;
import org.mozzes.swing.mgf.component.table.rendering.decorations.decorators.AlternatingRowColor;
import org.mozzes.swing.mgf.component.table.rendering.decorations.decorators.SelectedRowColoring;
import org.mozzes.swing.mgf.component.table.rendering.decorations.rules.column.ColumnNumberEquals;
import org.mozzes.swing.mgf.component.table.rendering.decorations.rules.value.InstanceOf;
import org.mozzes.swing.mgf.component.table.rendering.renderers.SpecificTranslatorCellRenderer;
import org.mozzes.swing.mgf.decoration.Decorator;
import org.mozzes.swing.mgf.decoration.decorators.AlignmentDecorator;
import org.mozzes.swing.mgf.translation.Translator;
import org.mozzes.utils.rulesengine.Rule;
import org.mozzes.utils.rulesengine.rules.TrueRule;


/**
 * Provides methods for setting up cell renderers and cell decorators
 * 
 * @author milos
 */
public class CellRenderingFacility {
	private static final List<Class<?>> primitives = new ArrayList<Class<?>>();
	private final JTable attachedTable;
	private final UniversalCellRenderer universalRenderer = new UniversalCellRenderer();;

	/**
	 * @param table Setups table to work with the facility
	 */
	public CellRenderingFacility(JTable table) {
		this.attachedTable = table;
		attachedTable.setDefaultRenderer(Object.class, universalRenderer);

		// Discard renderers for primitive types set on table directly (because some LAFs are setting them)
		initPrimitives();
		discardPrimitiveRenderers();
		initializeDefaultRenderers();

		initializeDefaultDecorators();
	}

	private void initializeDefaultRenderers() {
		Map<Class<?>, TableCellRenderer> defaultRenderers = DefaultCellRenderers.getDefaultRenderers();
		for (Entry<Class<?>, TableCellRenderer> entry : defaultRenderers.entrySet()) {
			setClassRenderer(entry.getKey(), entry.getValue());
		}
	}

	private void initializeDefaultDecorators() {
		addDecoration(new AlternatingRowColor());
		addDecoration(new SelectedRowColoring(attachedTable));
		// addDecoration(new AlternatingRowColor(Color.orange, Color.green));
		// addDecoration(new SelectedRowColoring(Color.BLUE, Color.white));
		addDecoration(new InstanceOf(Number.class), new AlignmentDecorator(SwingConstants.RIGHT));
		// addDecoration(new CellFocusBorder());
	}

	private void discardPrimitiveRenderers() {
		for (Class<?> primitive : primitives) {
			if (attachedTable.getDefaultRenderer(primitive) != universalRenderer)
				attachedTable.setDefaultRenderer(primitive, null);
		}
	}

	private void initPrimitives() {
		primitives.add(boolean.class);
		primitives.add(int.class);
		primitives.add(double.class);
		primitives.add(float.class);
		primitives.add(long.class);
		primitives.add(char.class);

		primitives.add(Boolean.class);
		primitives.add(Integer.class);
		primitives.add(Double.class);
		primitives.add(Float.class);
		primitives.add(Long.class);
		primitives.add(Character.class);

		primitives.add(String.class);
		primitives.add(Date.class);
		primitives.add(Number.class);
		primitives.add(Icon.class);
		primitives.add(ImageIcon.class);
	}

	/**
	 * @return Table attached to this facility instance
	 */
	public JTable getAttachedTable() {
		return attachedTable;
	}

	/**
	 * Sets a {@link TableCellRenderer} for a column
	 * 
	 * @param column Column for which to set the renderer
	 * @param renderer Renderer that should be set
	 */
	public void setColumnRenderer(int column, TableCellRenderer renderer) {
		getUniversalRenderer().setColumnRenderer(column, renderer);
	}

	/**
	 * Sets a {@link CellEditor} for a specific class
	 * 
	 * @param clazz Class for which to set the renderer
	 * @param renderer Renderer that should be set
	 */
	public void setClassRenderer(Class<?> clazz, TableCellRenderer renderer) {
		getUniversalRenderer().setClassRenderer(clazz, renderer);
	}

	private UniversalCellRenderer getUniversalRenderer() {
		TableCellRenderer defaultRenderer = attachedTable.getDefaultRenderer(Object.class);
		if (!(defaultRenderer instanceof UniversalCellRenderer))
			throw new IllegalStateException("Default cell renderer set to attached table is not UniversalCellRenderer!");
		return (UniversalCellRenderer) attachedTable.getDefaultRenderer(Object.class);
	}

	/**
	 * Adds a {@link RuleBasedCellDecorator} cell decorator to a whole table
	 */
	public void addDecoration(RuleBasedCellDecorator decorator) {
		getUniversalRenderer().addDecoration(decorator);
	}

	/**
	 * Adds a {@link RuleBasedCellDecorator} cell decorator to a column
	 */
	public void addDecoration(int column, RuleBasedCellDecorator decorator) {
		Rule<Context> rule = new ColumnNumberEquals(column);
		if (decorator.getGlobalRule() != null) {
			rule = rule.and(decorator.getGlobalRule());
		}
		decorator.setGlobalRule(rule);
		addDecoration(decorator);
	}

	/**
	 * Adds a {@link Decorator} to all table cells which satisfy the rule
	 */
	private void addDecoration(final Rule<Context> rule, final Decorator decorator) {
		addDecoration(new RuleBasedCellDecorator() {
			{
				setGlobalRule(rule);
				add(decorator);
			}
		});
	}

	/**
	 * Adds a {@link Decorator} to all table cells
	 */
	public void addDecoration(Decorator decorator) {
		addDecoration(new TrueRule<Context>(), decorator);
	}

	/**
	 * Adds a {@link Decorator} to all table cells in the specified colum
	 */
	public void addDecoration(int column, Decorator decorator) {
		addDecoration(new ColumnNumberEquals(column), decorator);
	}

	public <F> void setTranslator(int column, Translator<F, String> translator) {
		setColumnRenderer(column, new SpecificTranslatorCellRenderer<F>(translator));
	}
}