package org.mozzes.utils.misc;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

/**
 * This is a comparator for Date values.<br>
 * Comparation could be done in 2 flavours:<br>
 * 1. Compare only dates and ignore time parts
 * 2. Compare both date and time parts
 * By default it compares only dates part.<br>
 * To compare both date and time parts use constructor {@link #DateComparator(boolean)}<br>
 * How it works:<br>
 * <b>
 * compare(today,today) = 0<br>
 * compare(today,yesterday) = 1<br>
 * compare(today,tommorow) =  -1<br>
 * </b>
 * @author draganm
 */
public class DateComparator implements Comparator<Date>, Serializable {
	private static final long serialVersionUID = 1L;

	protected boolean ignoreTime;
	protected Calendar calendar1 = Calendar.getInstance();
	protected Calendar calendar2 = Calendar.getInstance();

	public DateComparator() {
		this(true);
	}

	public DateComparator(boolean ignoreTime) {
		calendar1 = Calendar.getInstance();
		calendar2 = Calendar.getInstance();
		setIgnoreTime(ignoreTime);
	}

	public boolean getIgnoreTime() {
		return ignoreTime;
	}

	public void setIgnoreTime(boolean flag) {
		ignoreTime = flag;
	}

	/**
	 * Compares two arguments.<br>
	 * Returns:<br>
	 * - negative integer if the first date argument is before than second.
	 * - zero if the first date argument is equal to second.
	 * - positive integer if the first date argument is after than the second.
	 * 
	 */
	@Override
	public int compare(Date d1, Date d2) {
		if (!ignoreTime) {
			long l;
			if ((l = (d1).getTime() - (d2).getTime()) == 0L)
				return 0;
			return l >= 0L ? 1 : -1;
		}
		calendar1.setTime(d1);
		calendar2.setTime(d2);
		int i;
		if ((i = calendar1.get(Calendar.ERA) - calendar2.get(Calendar.ERA)) == 0
				&& (i = calendar1.get(Calendar.YEAR) - calendar2.get(Calendar.YEAR)) == 0
				&& (i = calendar1.get(Calendar.MONTH) - calendar2.get(Calendar.MONTH)) == 0)
			i = calendar1.get(Calendar.DAY_OF_MONTH) - calendar2.get(Calendar.DAY_OF_MONTH);
		return i;
	}

}
