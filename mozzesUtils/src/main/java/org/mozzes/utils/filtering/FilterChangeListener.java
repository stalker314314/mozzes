package org.mozzes.utils.filtering;

/**
 * Interfejs koga implementiraju svi koji reaguju na promene filtera.
 * 
 * @author Perica Milosevic
 * @version 1.7.8
 */
public interface FilterChangeListener {

	/**
	 * Doslo je do promene filtera
	 * 
	 * @param filter Promenjeni Filter
	 */
	public void filterChanged(Filter<?> filter);

}
