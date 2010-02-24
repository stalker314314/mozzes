package org.mozzes.swing.mgf.datarenderer.table.filter;

/**
 * Table column filtering function
 * 
 * @author milos
 * 
 * @param <T> Type of an object that represents row
 * @param <S> Type of value contained in filtered column
 */
public interface ColumnFilter<T, S> {
	public boolean isAcceptable(T object, S fieldValue, String criteria);
}
