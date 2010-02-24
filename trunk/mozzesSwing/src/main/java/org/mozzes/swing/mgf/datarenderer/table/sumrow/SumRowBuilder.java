package org.mozzes.swing.mgf.datarenderer.table.sumrow;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;

import org.mozzes.swing.mgf.component.table.helpers.TablesColumnWidthSynchronizer;
import org.mozzes.swing.mgf.datamodel.DataModel;
import org.mozzes.swing.mgf.datamodel.fields.CalculatedField;
import org.mozzes.swing.mgf.datamodel.fields.StaticDataField;
import org.mozzes.swing.mgf.datamodel.fields.WholeObjectField;
import org.mozzes.swing.mgf.datamodel.impl.DefaultDataModel;
import org.mozzes.swing.mgf.datarenderer.table.TableRenderModel;
import org.mozzes.swing.mgf.datasource.SelectionListDataSource;
import org.mozzes.swing.mgf.datasource.impl.DefaultSelectionListDataSource;
import org.mozzes.swing.mgf.decoration.decorators.FontStyleDecorator;
import org.mozzes.swing.mgf.helpers.component.scrollbar.HorizontalScrollbarSynchronizer;
import org.mozzes.swing.mgf.helpers.datasource.BeanToListDataSource;
import org.mozzes.utils.reflection.ReflectionUtils;

import net.miginfocom.swing.MigLayout;


/**
 * Allows you to build and configure a sum row for a table
 * 
 * @author milos
 * 
 * @param <T> Type of the bean contained by one row of the table which is summed
 */
public class SumRowBuilder<T> {
	private static final FontStyleDecorator FONT_STYLE = new FontStyleDecorator(Font.BOLD);
	private TableRenderModel<List<T>> sumarumTableRenderModel;
	private final TableRenderModel<T> dataTableRM;
	private DataModel<List<T>> model;
	private SelectionListDataSource<List<T>> source;
	private boolean built = false;
	private JScrollPane scrollPane;
	private JPanel panel;

	private Component padding;
	private TableRenderModel<String> paddingTable;
	private Component rightPadding;
	private ComponentListener verticalScrollBarListener = new VerticalScrollBarListener();

	public SumRowBuilder(TableRenderModel<T> dataTableRM) {
		this.dataTableRM = dataTableRM;
		initModel();
		initSource();
		padding = getPaddingTable();
		rightPadding = getRightPaddingTable();
		setupRightPaddingListeners();
	}

	private void initSource() {
		source = new BeanToListDataSource<List<T>>(dataTableRM.getDataSource());
		dataTableRM.getFilteringFacility().getTableRowSorter().addRowSorterListener(new RowSorterListener() {
			@Override
			public void sorterChanged(RowSorterEvent e) {
				source.fireDataUpdatedEvent();
	}
		});
	}

	private void initModel() {
		model = new DefaultDataModel<List<T>>();
		for (int i = 0; i < dataTableRM.getDataModel().getFieldsNumber(); i++) {
			model.addField(new StaticDataField<List<T>, String>(
					dataTableRM.getDataModel().getField(i).getName(), String.class, ""));
		}
	}

	private void setupRightPaddingListeners() {
		dataTableRM.getScrollPane().getVerticalScrollBar()
				.addComponentListener(verticalScrollBarListener);
	}

	private void build() {
		if (built)
			return;
		built = true;

		sumarumTableRenderModel = new TableRenderModel<List<T>>(source, model, false);
		JTable table = sumarumTableRenderModel.getTable();
		TablesColumnWidthSynchronizer.setup(dataTableRM.getTable(), table);
		table.setCellSelectionEnabled(false);
		table.setRowSelectionAllowed(false);
		table.setColumnSelectionAllowed(false);
		table.setFocusable(false);
		sumarumTableRenderModel.getCellRenderingFacility().addDecoration(FONT_STYLE);
		syncAutoResizeMode();
		dataTableRM.getTable().addPropertyChangeListener("autoResizeMode", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				syncAutoResizeMode();
			}
		});
	}

	private void setupPanel() {
		if (panel != null)
			return;

		panel = new JPanel(new MigLayout(
				"ins 0, novisualpadding, gap 0 0, wrap 3",
				"[p!, fill][grow, fill][p!, fill]"));
		panel.add(padding);
		panel.add(scrollPane);
		panel.add(rightPadding);
		// panel.setBorder(dataTableRM.getScrollPane().getBorder());
		panel.setBorder(new LineBorder(Color.GRAY));
	}

	private void setupScrollPane() {
		build();
		if (scrollPane != null)
			return;
		scrollPane = sumarumTableRenderModel.getScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(null);
		scrollPane.setPreferredSize(new Dimension(0, getTable().getRowHeight()));
		HorizontalScrollbarSynchronizer.install(scrollPane, dataTableRM.getScrollPane());
	}

	private Component getPaddingTable() {
		DefaultDataModel<String> dataModel = new DefaultDataModel<String>();
		dataModel.addField(new WholeObjectField<String>(String.class));
		DefaultSelectionListDataSource<String> source =
				new DefaultSelectionListDataSource<String>(String.class);
		source.add("");
		paddingTable = new TableRenderModel<String>(source, dataModel, false);
		paddingTable.getCellRenderingFacility().addDecoration(FONT_STYLE);

		paddingTable.getTable().setRowSelectionAllowed(false);
		paddingTable.getTable().setCellSelectionEnabled(false);
		paddingTable.getTable().setColumnSelectionAllowed(false);
		paddingTable.getTable().setFocusable(false);
		paddingTable.getTable().setAutoscrolls(false);
		paddingTable.getTable().setTableHeader(null);
		paddingTable.getScrollPane().setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		paddingTable.getScrollPane().setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		paddingTable.getScrollPane().setBorder(null);

		return paddingTable.getRenderComponent();
	}

	private Component getRightPaddingTable() {
		DefaultDataModel<String> dataModel = new DefaultDataModel<String>();
		dataModel.addField(new WholeObjectField<String>(String.class));
		DefaultSelectionListDataSource<String> source =
				new DefaultSelectionListDataSource<String>(String.class);
		source.add("");
		TableRenderModel<String> rightPaddingTable = new TableRenderModel<String>(source, dataModel, false);
		rightPaddingTable.getCellRenderingFacility().addDecoration(FONT_STYLE);

		rightPaddingTable.getTable().setRowSelectionAllowed(false);
		rightPaddingTable.getTable().setCellSelectionEnabled(false);
		rightPaddingTable.getTable().setColumnSelectionAllowed(false);
		rightPaddingTable.getTable().setFocusable(false);
		rightPaddingTable.getTable().setAutoscrolls(false);
		rightPaddingTable.getTable().setTableHeader(null);
		rightPaddingTable.getScrollPane().setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		rightPaddingTable.getScrollPane().setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		rightPaddingTable.getScrollPane().setBorder(null);

		Component renderComponent = rightPaddingTable.getRenderComponent();
		// ((JComponent) renderComponent).setBorder(new LineBorder(Color.black));
		return renderComponent;
	}

	private void syncAutoResizeMode() {
		getTable().setAutoResizeMode(dataTableRM.getTable().getAutoResizeMode());
		if (getTable().getAutoResizeMode() == JTable.AUTO_RESIZE_OFF)
			setRightPadding(0);
	}

	/**
	 * Shows sum for the specified column (calculated automatically)
	 * 
	 * @param <F> Type of the column
	 * @param column Index of the column to be summed
	 * @throws IllegalArgumentException if the column does not contain {@link Number numbers}
	 */
	@SuppressWarnings("unchecked")
	public <F extends Number> SumRowBuilder<T> showSumFor(final int column) {
		final Class<F> fieldClass = (Class<F>) (Class<?>) ReflectionUtils.resolvePrimitiveType(
				dataTableRM.getDataModel().getField(column).getFieldType());
		if (!Number.class.isAssignableFrom(fieldClass)) {
			throw new IllegalArgumentException("Column " + column + " does not contain numbers!");
		}

		model.removeField(column);
		model.addField(column, new DefaultSumField(fieldClass, column));
		return this;
	}

	/**
	 * Shows sum for the specified column (calculated automatically)
	 * 
	 * @param <F> Type of the column
	 * @param name Name of the column to be summed
	 * @throws IllegalArgumentException if the column does not contain {@link Number numbers}
	 */
	public <F extends Number> SumRowBuilder<T> showSumFor(String name) {
		int index = dataTableRM.getDataModel().indexOf(name);
		if (index == -1)
			throw new IllegalArgumentException("Field with specified name(" + name + ") is not contained by the model!");
		showSumFor(index);
		return this;
	}

	/**
	 * Sets a function which calculates the sum for specified column<br/>
	 * (and shows the sum in appropriate cell)
	 * 
	 * @param <F> Type of the resulting sum object
	 * @param name Name of the column to be summed
	 * @param sumField Calculation method for sum (see {@link CalculatedField})
	 */
	public <F> SumRowBuilder<T> setSumFunction(String name, SumCalculationField<T, F> sumField) {
		int index = dataTableRM.getDataModel().indexOf(name);
		if (index == -1)
			throw new IllegalArgumentException("Field with specified name(" + name + ") is not contained by the model!");
		setSumFunction(index, sumField);
		return this;
	}

	/**
	 * Sets a function which calculates the sum for specified column<br/>
	 * (and shows the sum in appropriate cell)
	 * 
	 * @param <F> Type of the resulting sum object
	 * @param column Index of the column to be summed
	 * @param sumField Calculation method for sum (see {@link CalculatedField})
	 */
	public <F> SumRowBuilder<T> setSumFunction(int column, SumCalculationField<T, F> sumField) {
		model.removeField(column);
		model.addField(column, sumField);
		sumField.setBuilder(this);
		return this;
	}

	/**
	 * Sets text to a cell (if cell contains sum, sum will be cleared)
	 * 
	 * @param column Index of the column
	 * @param text Text that will be displayed
	 */
	public SumRowBuilder<T> setCellText(int column, String text) {
		model.removeField(column);
		model.addField(column, new StaticDataField<List<T>, String>(String.class, text));
		return this;
	}

	/**
	 * Sets text to a cell (if cell contains sum, sum will be cleared)
	 * 
	 * @param name Name of the column
	 * @param text Text that will be displayed
	 */
	public SumRowBuilder<T> setCellText(String name, String text) {
		int index = dataTableRM.getDataModel().indexOf(name);
		if (index == -1)
			throw new IllegalArgumentException("Field with specified name(" + name + ") is not contained by the model!");
		model.removeField(index);
		model.addField(index, new StaticDataField<List<T>, String>(String.class, text));
		return this;
	}

	/**
	 * Sets text to a padding cell
	 * 
	 * @param text Text that will be displayed
	 */
	public void setPaddingText(String text) {
		paddingTable.getDataSource().set(0, text);
	}

	/**
	 * @return Text from the padding cell
	 */
	public String getPaddingText() {
		return paddingTable.getDataSource().get(0);
	}

	/**
	 * @return Width of the padding cell
	 */
	public int getPadding() {
		return (int) padding.getPreferredSize().getWidth();
	}

	/**
	 * @param width Width of the padding cell
	 */
	public void setPadding(int width) {
		int height = getTable().getRowHeight();
		padding.setPreferredSize(new Dimension(width, height));
		padding.setMaximumSize(new Dimension(width, height));
		padding.setMinimumSize(new Dimension(width, height));
		if (panel != null)
			panel.revalidate();
	}

	/**
	 * @param width Width of the right padding cell
	 */
	private void setRightPadding(int width) {
		int height = getTable().getRowHeight();
		rightPadding.setPreferredSize(new Dimension(width, height));
		rightPadding.setMaximumSize(new Dimension(width, height));
		rightPadding.setMinimumSize(new Dimension(width, height));
		if (panel != null)
			panel.revalidate();
	}

	/**
	 * @return Table with sum row
	 */
	public JTable getTable() {
		build();
		return sumarumTableRenderModel.getTable();
	}

	/**
	 * @return Panel with properly setup layout<br/>
	 *         Contains all of the components required to show sum row
	 */
	public Component getRenderComponent() {
		build();
		int width = dataTableRM.isRowNumberColumnVisible() ?
				dataTableRM.getRowNumberColumnWidth() : 0;
		setPadding(width);
		setRightPadding(0);

		getTable().setTableHeader(null);
		setupScrollPane();
		setupPanel();

		return panel;
	}

	/**
	 * @return {@link TableRenderModel} of the table which contains sum row
	 */
	public TableRenderModel<List<T>> getRenderModel() {
		build();
		return sumarumTableRenderModel;
	}

	public TableRenderModel<T> getAttachedTableRenderModel() {
		return dataTableRM;
}

	private class DefaultSumField<F extends Number> extends SumCalculationField<T, F> {
		private final int column;

		public DefaultSumField(Class<F> fieldType, int column) {
			super(fieldType);
			this.column = column;
			setBuilder(SumRowBuilder.this);
		}

		@Override
		public F getSum(List<T> object) {
			if (object == null || object.isEmpty())
				return ReflectionUtils.getDefaultValueOf(ReflectionUtils.resolveToPrimitiveType(getFieldType()));
			double sum = 0;
			for (T t : object) {
				Object value = dataTableRM.getDataModel().getField(column).getValue(t);
				if (value instanceof Number)
					sum += ((Number) value).doubleValue();
			}
			return ReflectionUtils.convertNumber(sum, getFieldType());
		}
	}

	private class VerticalScrollBarListener extends ComponentAdapter {
		@Override
		public void componentHidden(ComponentEvent e) {
			setRightPadding(0);
		}

		@Override
		public void componentShown(ComponentEvent e) {
			if (dataTableRM.getTable().getAutoResizeMode() == JTable.AUTO_RESIZE_OFF)
				return;
			setRightPadding(dataTableRM.getScrollPane().getVerticalScrollBar().getWidth());
		}
	}

}
