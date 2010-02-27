/*
 * Copyright 2010 Mozzart
 *
 *
 * This file is part of mozzes.
 *
 * mozzes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mozzes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mozzes.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.mozzes.swing.mgf.datasource;

import java.util.List;

import org.mozzes.swing.mgf.datasource.impl.SelectionMode;


/**
 * Provides a list of the beans also giving the ability to define selection on the level of the data source, and
 * triggering selection events
 * 
 * @author milos
 * 
 * @param <T> Type of the bean contained by the list
 */
public interface SelectionListDataSource<T> extends ListDataSource<T> {

	/**
	 * Deselects any selected objects
	 */
	public void clearSelection();

	/**
	 * Clears the selection then sets objects at specified
	 * <i>indexes</i> as selected<br>
	 * <b>If the selection mode is {@link SelectionMode#SINGLE_SELECTION SINGLE_SELECTION} selects only object from the
	 * first specified index</b>
	 * 
	 * @param indices Indices at which objects should be selected
	 */
	public void setSelectedIndices(int... indices);

	/**
	 * Clears the selection then sets objects at specified
	 * <i>indexes</i> as selected<br>
	 * <b>If the selection mode is {@link SelectionMode#SINGLE_SELECTION SINGLE_SELECTION} selects only object from the
	 * first specified index</b>
	 * 
	 * @param indices Indices at which objects should be selected
	 */
	public void setSelectedIndices(List<Integer> indices);
	
	/**
	 * Sets objects at specified <i>indexes</i> as selected(but also keeps all previously selected elements)
	 * 
	 * @param indices Indices at which objects should be selected
	 * @throws UnsupportedOperationException if the selection mode is {@link SelectionMode#SINGLE_SELECTION
	 *             SINGLE_SELECTION}
	 */
	public void addSelectedIndices(int... indices);

	/**
	 * Deselects objects at specified <i>indexes</i>
	 * 
	 * @param indices Indices at which objects should be deselected
	 */
	public void removeSelectedIndices(int... indices);

	/**
	 * {@link SelectionListDataSource#clearSelection() Clears the selection} then selects objects at indexes between
	 * <i>from</i> and <b>to</b> (from and to included)
	 * 
	 * @param from Index at which the selection interval starts
	 * @param to Index at which the selection interval ends
	 * @throws UnsupportedOperationException if the selection mode is {@link SelectionMode#SINGLE_SELECTION
	 *             SINGLE_SELECTION}
	 */
	public void setSelectionInterval(int from, int to);

	/**
	 * Selects objects at indexes between <i>from</i> and <b>to</b> (from and to included)<br>
	 * (Also keeps all previously selected elements)
	 * 
	 * @param from Index at which the selection interval starts
	 * @param to Index at which the selection interval ends
	 * @throws UnsupportedOperationException if the selection mode is {@link SelectionMode#SINGLE_SELECTION
	 *             SINGLE_SELECTION}
	 */
	public void addSelectionInterval(int from, int to);

	/**
	 * Deselects objects at indexes between <i>from</i> and <b>to</b> (from and to included)<br>
	 * 
	 * @param from Index at which the selection interval starts
	 * @param to Index at which the selection interval ends
	 */
	public void removeSelectionInterval(int from, int to);

	/**
	 * Toggles the selection of objects on specified indexes
	 * 
	 * @param indices Indexes at which selection of objects should be toggled
	 * @throws IllegalArgumentException if the selection mode is {@link SelectionMode#SINGLE_SELECTION SINGLE_SELECTION}
	 *             and resulting selection will include more than one object
	 */
	public void toggleSelectedIndices(int... indices);

	/**
	 * Inverts the selection
	 * 
	 * @throws UnsupportedOperationException if the selection mode is {@link SelectionMode#SINGLE_SELECTION
	 *             SINGLE_SELECTION}
	 */
	public void invertSelection();

	/**
	 * Selects all the elements
	 * 
	 */
	public void selectAll();

	/**
	 * @return Index of selected item
	 * @throws UnsupportedOperationException if the {@link SelectionListDataSource#getSelectionMode() selection mode} is
	 *             {@link SelectionMode#MULTIPLE_SELECTION MULTIPLE_SELECTION}
	 */
	public int getSelectedIndex() throws UnsupportedOperationException;

	/**
	 * @return Indexes of selected objects
	 */
	public List<Integer> getSelectedIndices();

	/**
	 * @return Selected object
	 * @throws UnsupportedOperationException if the {@link SelectionListDataSource#getSelectionMode() selection mode} is
	 *             {@link SelectionMode#MULTIPLE_SELECTION MULTIPLE_SELECTION}
	 */
	public T getSelectedValue() throws UnsupportedOperationException;

	/**
	 * @return Unmodifiable List of selected objects
	 */
	public List<T> getSelectedValues();

	/**
	 * Sets the selection mode
	 * 
	 * @param mode {@link SelectionMode} to be set
	 */
	public void setSelectionMode(SelectionMode mode);

	/**
	 * @return {@link SelectionMode} currently in use
	 */
	public SelectionMode getSelectionMode();

	/**
	 * @return {@link ListDataSource Data source} providing currently selected objects
	 */
	public ListDataSource<T> getSelectedValuesDataSource();

	/**
	 * @return {@link ListDataSource Data source} providing indices of currently selected objects
	 */
	public ListDataSource<Integer> getSelectedIndicesDataSource();

	/**
	 * @return {@link BeanDataSource Data source} providing currently selected object
	 */
	public BeanDataSource<T> getSelectedValueDataSource();

	/**
	 * @return {@link BeanDataSource Data source} providing index of currently selected object
	 */
	public BeanDataSource<Integer> getSelectedIndexDataSource();

	/**
	 * Bind selection of this {@link SelectionListDataSource source} to selection of the specified
	 * {@link SelectionListDataSource source}
	 * 
	 * @param other {@link SelectionListDataSource Source} to which the selection should be bound
	 */
	public void bindSelection(SelectionListDataSource<T> other);

}
