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
package org.mozzes.swing.mgf.datasource.events;

/**
 * Represents the base class for all DataSource events<br>
 * <b>[NOTICE] All Event instances have to be immutable(except this "propagated" property)</b>
 * 
 * @author milos
 * 
 * @param <T> The type of the object which DataSource provides
 */
public abstract class DataSourceEvent<T> {
	private boolean propagated;

	public boolean isPropagated() {
		return propagated;
	}

	/**
	 * FOR INTERNAL USE BY MGF FRAMEWORK ONLY!!!<br>
	 * Do not use for any reason, there must be another way to solve your problem!!!
	 */
	public void setPropagated(boolean propagated) {
		this.propagated = propagated;
	}
}
