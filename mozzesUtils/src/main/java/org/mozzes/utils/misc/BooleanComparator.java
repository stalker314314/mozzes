package org.mozzes.utils.misc;

import java.io.Serializable;
import java.util.Comparator;

/**
 * This is a comparator for Boolean values which treats TRUE as a greater value than FALSE.<br>
 * Thus:<br>
 * <b>
 * compare(true,true) = compare(false,false) = 0<br>
 * compare(true,false) = 1<br>
 * compare(false,true) =  -1<br>
 * </b>
 * @author milos
 */
public class BooleanComparator implements Comparator<Boolean>, Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public int compare(Boolean o1, Boolean o2) {
		int b1 = o1.booleanValue() ? 0 : 1;
		int b2 = o2.booleanValue() ? 0 : 1;

		return b2 - b1;
	}

}

