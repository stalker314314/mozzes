package org.mozzes.swing.mgf.component.table.rendering;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.mozzes.swing.mgf.component.table.rendering.decorations.CellDecorationRule;
import org.mozzes.swing.mgf.component.table.rendering.decorations.RuleBasedCellDecorator;
import org.mozzes.swing.mgf.component.table.rendering.decorations.RuleBasedDecorationsManager;
import org.mozzes.swing.mgf.component.table.rendering.decorations.CellDecorationRule.Context;
import org.mozzes.swing.mgf.component.table.rendering.renderers.DefaultCellRendererWithTranslation;
import org.mozzes.swing.mgf.decoration.Decorator;
import org.mozzes.utils.ClassBasedObjectMappingRepository;


public class UniversalCellRenderer implements TableCellRenderer {
	private final ClassBasedObjectMappingRepository<TableCellRenderer> repository;
	private final Map<Integer, TableCellRenderer> columnRenderers = new HashMap<Integer, TableCellRenderer>();

	private final CellDecorationRule.Context context =
			new CellDecorationRule.Context(null, null, null, false, false, 0, 0);
	private final RuleBasedDecorationsManager ruleBasedDecorationsManager =
			new RuleBasedDecorationsManager();
	private JComponent lastComponent;
	private Context lastContext =
			new CellDecorationRule.Context(null, null, null, false, false, 0, 0);;

	public UniversalCellRenderer() {
		repository = new ClassBasedObjectMappingRepository<TableCellRenderer>(
				new DefaultCellRendererWithTranslation());
	}

	public void setClassRenderer(Class<?> clazz, TableCellRenderer renderer) {
		repository.setObjects(clazz, renderer);
	}

	public void setColumnRenderer(int column, TableCellRenderer renderer) {
		columnRenderers.put(column, renderer);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Component component;

		if (columnRenderers.get(column) != null) {
			// Get column based
			component = columnRenderers.get(column)
					.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		} else {
			// Get class based
			Class<? extends Object> valueClass = value == null ? table.getModel().getColumnClass(column) : value
					.getClass();
			component = repository.getObject(valueClass)
					.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}

		if (!(component instanceof JComponent))
			return component;
		// else apply decoration
		if (lastComponent != null && lastContext != null) {
			if (lastContext.getRow() < table.getRowCount()) {
			ruleBasedDecorationsManager.clearRuleDecorations(lastComponent, lastContext);
			}
		}
		context.setAll((JComponent) component, table, value, isSelected, hasFocus, row, column);

		ruleBasedDecorationsManager.applyDecoration(context, (JComponent) component);
		lastComponent = (JComponent) component;
		
		lastContext.setAll((JComponent) component, table, value, isSelected, hasFocus, row, column);
		return component;
	}

	public UniversalCellRenderer addDecoration(RuleBasedCellDecorator decorator) {
		ruleBasedDecorationsManager.add(decorator);
		return this;
	}

	public UniversalCellRenderer addDecoration(final Decorator decorator) {
		return addDecoration(new RuleBasedCellDecorator() {
			{
				add(decorator);
			}
		});
	}

}
