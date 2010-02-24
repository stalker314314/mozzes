package org.mozzes.swing.component.table;

import javax.swing.JTable;

/**
 * [AMD-57]
 * MTable Cell renderer za kolone sa proizvoljnim poravnanjem teksta.
 * 
 */
public class MAlignedTableCellRenderer extends MDefaultTableCellRenderer {

	private static final long serialVersionUID = 5769524582663361700L;
	
	/**
	 * Poravnanje teksta.
	 */
	private int alignment;

	/**
	 * Param konstruktor.
	 * @param orderColumn - da li se upotrebljava kolona sa rednim brojem.
	 */
	public MAlignedTableCellRenderer(Boolean orderColumn, int alignment) {
		super(orderColumn);
		this.alignment = alignment;
	}

	/**
	 * Custom obrada prikaza.
	 */
	@Override
	protected void doCustom(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		setHorizontalAlignment(alignment);
	}
}
