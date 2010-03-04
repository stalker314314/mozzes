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
package org.mozzes.swing.mgf;

public class DummyClass extends DummyAncesstor implements DummyInterface {
	private Integer intAttr;
	private DummyClass dummyClassAttr;
	private Integer noSetter;
	private String strAttr;
	private boolean primitiveBool;
	private Boolean wrapperBool;
	private double doubleValue;

	public DummyClass() {
	}

	public DummyClass(Integer i) {
		intAttr = i;
	}

	public DummyClass(DummyClass dummyClassAttr) {
		this.dummyClassAttr = dummyClassAttr;
	}

	public DummyClass setIntAttr(Integer intAttr) {
		this.intAttr = intAttr;
		return this;
	}

	public DummyClass(Integer intAttr, DummyClass dummyClassAttr) {
		this.intAttr = intAttr;
		this.dummyClassAttr = dummyClassAttr;
	}

	public Integer getIntAttr() {
		return intAttr;
	}

	@Override
	public String toString() {
		return "DummyClass [intAttr=" + intAttr + "]";
	}

	public DummyClass setDummyClassAttr(DummyClass dummyClassAttr) {
		this.dummyClassAttr = dummyClassAttr;
		return this;
	}

	public DummyClass getDummyClassAttr() {
		return dummyClassAttr;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dummyClassAttr == null) ? 0 : dummyClassAttr.hashCode());
		result = prime * result + ((intAttr == null) ? 0 : intAttr.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DummyClass))
			return false;
		DummyClass other = (DummyClass) obj;
		if (dummyClassAttr == null) {
			if (other.dummyClassAttr != null)
				return false;
		} else if (!dummyClassAttr.equals(other.dummyClassAttr))
			return false;
		if (intAttr == null) {
			if (other.intAttr != null)
				return false;
		} else if (!intAttr.equals(other.intAttr))
			return false;
		return true;
	}

	public Integer getNoSetter() {
		return noSetter;
	}

	public DummyClass setStrAttr(String strAttr) {
		this.strAttr = strAttr;
		return this;
	}

	public String getStrAttr() {
		return strAttr;
	}

	public DummyClass setPrimitiveBool(boolean primitiveBool) {
		this.primitiveBool = primitiveBool;
		return this;
	}

	public boolean isPrimitiveBool() {
		return primitiveBool;
	}

	public DummyClass setWrapperBool(Boolean wrapperBool) {
		this.wrapperBool = wrapperBool;
		return this;
	}

	public Boolean getWrapperBool() {
		return wrapperBool;
	}

	public DummyClass setDoubleValue(double doubleValue) {
		this.doubleValue = doubleValue;
		return this;
	}

	public double getDoubleValue() {
		return doubleValue;
	}
}
