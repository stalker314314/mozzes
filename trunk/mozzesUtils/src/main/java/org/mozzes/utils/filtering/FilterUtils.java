package org.mozzes.utils.filtering;

import java.util.ArrayList;
import java.util.List;

public class FilterUtils {

	/**
	 * Primenjuje se filter na prosledjenu listu i vraca rezultujuca lista.
	 * 
	 * @param inputList - lista koja se filtrira
	 * @param filter - ako je null vraca se originalna lista
	 * @return filtrirana lista
	 */
	public static <T> List<T> getFilteredList(List<T> inputList, Filter<T> filter) {
		List<T> filteredList = inputList;

		if (filter != null && inputList != null) {
			filteredList = new ArrayList<T>();

			for (T objectFromList : inputList) {
				if (filter.isAcceptable(objectFromList))
					filteredList.add(objectFromList);
			}
		}

		return filteredList;
	}

}
