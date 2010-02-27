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
package org.mozzes.swing.mgf.localization;

public enum LocalizationKey {
	TEST,
	TEST1,
	TEST2,
	// Do not remove test variables or change their key or value!
	TABLE_FILTER_LABEL,
	NO_DATA_MESSAGE,
	INVALID_NUMBER_FORMAT_MESSAGE,
	INVALID_DATE_FORMAT_MESSAGE,
	POSITIVE_NUMBER_MESSAGE,
	MAX_DECIMALS_EXCEEDED;

	public String getKey() {
		return this.toString();
	}
}
