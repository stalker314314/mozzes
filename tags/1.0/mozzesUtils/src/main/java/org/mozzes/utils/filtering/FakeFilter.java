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
package org.mozzes.utils.filtering;

/**
 * Kvazi-filter koji prihvata sve objekte
 * 
 * @author Perica Milosevic
 * @version 1.7
 */
public class FakeFilter<T> implements Filter<T> {

	public boolean isAcceptable(T object) {
		return true;
	}

	@Override
	public boolean equals(Object that) {
		return (that instanceof FakeFilter<?>);
	}

	@Override
	public int hashCode() {
		return 1;
	}
}
