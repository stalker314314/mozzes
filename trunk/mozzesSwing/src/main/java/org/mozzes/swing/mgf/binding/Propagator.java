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
package org.mozzes.swing.mgf.binding;

/**
 * Defines interface that all listeners used for propagation of changes between the source and bound component must
 * implement
 * 
 * @author milos
 * 
 */
public interface Propagator {
	/**
	 * Enables propagation through this channel
	 */
	public void enable();

	/**
	 * Disables propagation through this channel
	 */
	public void disable();

	/**
	 * @return <b>true</b> if propagation through this channel is allowed, false otherwise
	 */
	public boolean isEnabled();
}
