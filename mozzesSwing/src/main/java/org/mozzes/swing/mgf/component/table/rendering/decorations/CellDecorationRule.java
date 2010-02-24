package org.mozzes.swing.mgf.component.table.rendering.decorations;

import javax.swing.JComponent;
import javax.swing.JTable;

import org.mozzes.swing.mgf.datarenderer.table.CustomTableModel;
import org.mozzes.swing.mgf.datasource.ListDataSource;
import org.mozzes.utils.rulesengine.Rule;


public abstract class CellDecorationRule extends Rule<CellDecorationRule.Context> {

	public static class Context {
		private JComponent component;
		private JTable table;
		private Object value;
		private boolean isSelected;
		private boolean hasFocus;
		private int row;
		private int column;

		public Context(JComponent component, JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			super();
			this.component = component;
			this.table = table;
			this.value = value;
			this.isSelected = isSelected;
			this.hasFocus = hasFocus;
			this.row = row;
			this.column = column;
		}

		public JComponent getComponent() {
			return component;
		}

		public void setComponent(JComponent component) {
			this.component = component;
		}

		public JTable getTable() {
			return table;
		}

		public void setTable(JTable table) {
			this.table = table;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		public boolean isSelected() {
			return isSelected;
		}

		public void setSelected(boolean isSelected) {
			this.isSelected = isSelected;
		}

		public boolean isHasFocus() {
			return hasFocus;
		}

		public void setHasFocus(boolean hasFocus) {
			this.hasFocus = hasFocus;
		}

		public int getRow() {
			return row;
		}

		public void setRow(int row) {
			this.row = row;
		}

		public int getColumn() {
			return column;
		}

		public void setColumn(int column) {
			this.column = column;
		}

		public void setAll(JComponent component, JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			this.component = component;
			this.table = table;
			this.value = value;
			this.isSelected = isSelected;
			this.hasFocus = hasFocus;
			this.row = row;
			this.column = column;
		}

		public int getModelRow() {
			return getTable().convertRowIndexToModel(getRow());
	}

		@SuppressWarnings("unchecked")
		public <T> T getRowObject(Class<T> clazz) {
			if (clazz == null)
				throw new IllegalArgumentException("Clazz parameter cannot be null!");

			if (!(getTable().getModel() instanceof CustomTableModel<?>))
				throw new IllegalStateException("Attached table is not set-up by MGF, " +
						"and is not connected to a DataSource!");
			ListDataSource<?> source = ((CustomTableModel<?>) getTable().getModel()).getDataSource();
			if (!clazz.isAssignableFrom(source.getElementType()))
				throw new IllegalArgumentException(String.format(
						"The type you specified(%s) does not match the type the DataSource(%s) " +
						"connected to this table!", clazz.getSimpleName(),
						source.getElementType().getSimpleName()));
			Object object = source.get(getModelRow());
			return (T) object;
	}
}
}
