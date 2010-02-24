package org.mozzes.swing.mgf.component.table.rendering.decorations.rules.rows;

import java.util.Date;

import org.mozzes.swing.mgf.component.table.rendering.decorations.CellDecorationRule;
import org.mozzes.utils.misc.DateComparator;


public class DateInColumnBeforeThan extends CellDecorationRule {

	private Date than;
	private final int column;
	
	/**
	 * Constructor
	 * 
	 * @param column Column index which value is being compared / evaluated
	 * @param value Value that is compared with value at provided colum
	 */
	public DateInColumnBeforeThan(int column, Date value) {
		if (value == null)
			throw new IllegalStateException("Value must not be null");
		
		this.column = column;
		
		than = value;
	}

	
	@Override
	public boolean appliesTo(Context context) {
		Object valueAt = context.getTable().getValueAt(context.getRow(), column);
		
		if (valueAt == null && than == null)
			return true;
		else if (valueAt == null || than == null)
			return false;

		DateComparator dc = new DateComparator();
		return dc.compare((Date)valueAt, than) < 0;
	}

}
