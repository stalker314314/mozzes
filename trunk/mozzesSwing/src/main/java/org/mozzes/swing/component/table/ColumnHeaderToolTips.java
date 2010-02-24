package org.mozzes.swing.component.table;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;

/**
 * Display a tool tip for a table column header. This class is writed according to
 * http://www.exampledepot.com/egs/javax.swing.table/ColHeadTips.html .
 * <p>
 * Example of use:<br>
 * <pre>
 * ColumnHeaderToolTips columnHeaderToolTips = new ColumnHeaderToolTips(jTable);
 * columnHeaderToolTips.setToolTip(TableModel.SOME_COLUMN_1_INDEX, "Some tooltip 1");
 * columnHeaderToolTips.setToolTip(TableModel.SOME_COLUMN_2_INDEX, "Some tooltip 2");
 * ...
 * </pre>
 * </p>
 * @author adnan
 * 
 */
public class ColumnHeaderToolTips extends MouseMotionAdapter {

	/**
	 * Current column whose tooltip is being displayed. This variable is used to minimize the calls to setToolTipText().
	 */
	private Integer currentColumnIndex;

	/**
	 * Maps TableColumn objects to tooltips.
	 */
	private Map<Integer, String> tooltips = new HashMap<Integer, String>();

	public ColumnHeaderToolTips(JTable jTable) {
		jTable.getTableHeader().addMouseMotionListener(this);
	}

	/**
	 * Sets specified tooltip on the header of column with specified column index.
	 * 
	 * @param columnIndex - index of column for which should set column header tooltip
	 * @param tooltip - tooltip for table header
	 */
	public void setToolTip(int columnIndex, String tooltip) {
		if (tooltip == null) {
			tooltips.remove(columnIndex);
		} else {
			tooltips.put(columnIndex, tooltip);
		}
	}

	/**
	 * Returns tooltip of column header with specified column index.
	 * 
	 * @param columnIndex - index of column for which should returns column header tooltip
	 * @return tooltip
	 */
	public String getToolTip(int columnIndex) {
		return tooltips.get(columnIndex);
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		JTableHeader jTableHeader = (JTableHeader) event.getSource();
		setToolTip(jTableHeader, getColumnIndex(jTableHeader, event));
	}

	/**
	 * Returns index of column at which header currently is the mouse pointer.
	 * 
	 * @param jTableHeader - table header
	 * @param evt - mouse event
	 * @return index of column at which header currently is the mouse pointer
	 */
	private int getColumnIndex(JTableHeader jTableHeader, MouseEvent evt) {
		return jTableHeader.getTable().getColumnModel().getColumnIndexAtX(evt.getX());
	}

	/**
	 * Sets tooltip for specified table header at column with specified column index.
	 * 
	 * @param jTableHeader - table header
	 * @param columnIndex - index of column
	 */
	private void setToolTip(JTableHeader jTableHeader, int columnIndex) {
		if (columnIndex < 0 || isCurrentColumn(columnIndex)) {
			return;
		}
		currentColumnIndex = Integer.valueOf(columnIndex);
		jTableHeader.setToolTipText(tooltips.get(currentColumnIndex));
	}

	/**
	 * Returns true if the specified column index is index of column whose tooltip is currently displayed, otherwise
	 * false.
	 * 
	 * @param columnIndex - column index
	 * @return true if the specified column index is index of column whose tooltip is currently displayed, otherwise
	 *         false
	 */
	private boolean isCurrentColumn(int columnIndex) {
		return currentColumnIndex != null && columnIndex == currentColumnIndex.intValue();
	}

}
