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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 * Apstraktni filter koji se filtrira objekte u zavisnosti od vrednosti nekog njihovog property-ja.
 * 
 * @author Perica Milosevic
 * @version 1.7
 */
public abstract class PropertyFilter<ObjectType, PropertyType> implements Filter<ObjectType> {

	/**
	 * PropertyHolder vraca vrednost propertija za prosledjeni objekat
	 */
	private PropertyHolder propertyHolder = null;

	/**
	 * Vrednost sa kojom ce se porediti property
	 */
	private PropertyType compareValue = null;

	/**
	 * @param propertyName Naziv property-ja, dozvoljen je i format property.childProperty.childProperty
	 * @param compareValue Vrednost sa kojom se vrsi poredjenje
	 */
	public PropertyFilter(String propertyName, PropertyType compareValue) {
		setPropertyHolder(propertyName);
		setCompareValue(compareValue);
	}

	/**
	 * @return Vrednost sa kojom se poredi vrenost propertija
	 */
	public PropertyType getCompareValue() {
		return compareValue;
	}

	/**
	 * Vraca vrednost property-ja za prosledjeni objekat
	 * 
	 * @param object objekat ciji se property ocitava
	 * @return vrednost property-ja u okviru prosledjenog objekta
	 */
	@SuppressWarnings("unchecked")
	public PropertyType getPropertyValue(ObjectType object) {

		return (PropertyType) this.propertyHolder.getPropertyValue(object);
	}

	/**
	 * Vraca kolekciju vrednosti property-ja za prosledjeni objekat\n [Napomena] Koristiti samo ako property jeste
	 * kolekcija PropertyType objekata
	 * 
	 * @param object objekat ciji se property ocitava
	 * @return vrednost property-ja u okviru prosledjenog objekta
	 */
	@SuppressWarnings("unchecked")
	public Collection<PropertyType> getPropertyValueCollection(ObjectType object) {

		return (Collection<PropertyType>) this.propertyHolder.getPropertyValue(object);
	}

	private void setCompareValue(PropertyType compareValue) {
		this.compareValue = compareValue;
	}

	private void setPropertyHolder(String propertyName) {
		this.propertyHolder = new PropertyHolder(getPropertyList(propertyName));
	}

	@Override
	public int hashCode() {
		return propertyHolder.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!this.getClass().isInstance(o))
			return false;

		if (!(o instanceof PropertyFilter<?, ?>))
			return false;

		PropertyFilter<?, ?> that = (PropertyFilter<?, ?>) o;
		return this.propertyHolder.equals(that.propertyHolder);
	}

	/**
	 * Parsira naziv propertija u obliku: property1.property2.property3 i vraca u ga u obliku ulancane liste gde su
	 * nazivi propertija u obrnutom redosledu: property3, property2, property1
	 * 
	 * @param propertyName Naziv propertyja koji se parsira
	 * @return Lista propertija
	 */
	private LinkedList<String> getPropertyList(String propertyName) {
		if (propertyName == null || propertyName.length() == 0)
			throw new IllegalArgumentException();

		StringTokenizer tokenizer = new StringTokenizer(propertyName, ".", false);
		LinkedList<String> properties = new LinkedList<String>();
		while (tokenizer.hasMoreTokens()) {
			properties.addFirst(tokenizer.nextToken());
		}
		return properties;
	}

	private static class PropertyHolder {

		private String propertyName = null;
		private PropertyHolder parentPropertyHolder = null;

		/**
		 * Field se kesira - odredjuje se na osnovu prvog objekta koji stigne do filtera
		 */
		private Field cachedField = null;

		protected PropertyHolder(LinkedList<String> properties) {
			String propertyName = properties.removeFirst();
			if (propertyName == null)
				throw new NullPointerException();

			this.propertyName = propertyName;

			if (properties.size() > 0)
				this.parentPropertyHolder = new PropertyHolder(properties);
			else
				this.parentPropertyHolder = null;
		}

		/**
		 * Vraca vrednost zahtevanog property-ja kod prosledjenog objekta
		 * 
		 * @param object objekat ciji se property cita
		 * @return vrednost zahtevanog property-ja kod prosledjenog objekta
		 */
		private Object getPropertyValue(Object object) {
			Object anObject = object;

			if (anObject == null)
				throw new NullPointerException();

			// ukoliko imamo roditelja prvo uzimamo njihovu vrednost
			if (parentPropertyHolder != null)
				anObject = parentPropertyHolder.getPropertyValue(anObject);

			try {
				Field field = getField(anObject);
				final boolean accessible = field.isAccessible();
				field.setAccessible(true);
				Object returnValue = field.get(anObject);
				field.setAccessible(accessible);
				return returnValue;
			} catch (SecurityException e) {
				throwPropertyInfoException(e);
			} catch (NoSuchFieldException e) {
				throwPropertyInfoException(e);
			} catch (IllegalArgumentException e) {
				throwPropertyInfoException(e);
			} catch (IllegalAccessException e) {
				throwPropertyInfoException(e);
			}

			// ovde nikada nece doci
			throw new RuntimeException("Invalid property filter state");
		}

		private Field getField(Object object) throws NoSuchFieldException {
			if (cachedField == null) {
				Class<?> clazz = object.getClass();
				cachedField = createField(clazz);
			}

			return cachedField;
		}

		/**
		 * Pronalazi Field na osnovu klase i naziva property-ja
		 * 
		 * @param clazz Klasa kojoj property pripada
		 * @return Field koji se koristi za ocitavnje vrednosti
		 * @throws NoSuchFieldException Ukoliko property ne postoji u klasi
		 */
		private Field createField(Class<?> clazz) throws NoSuchFieldException {
			try {
				return clazz.getDeclaredField(propertyName);
			} catch (NoSuchFieldException e) {

				// rekurzivno trazimo polje u klasama koje su nasledjene
				Class<?> superClass = clazz.getSuperclass();
				if (superClass != null)
					try {
						return createField(superClass);
					} catch (NoSuchFieldException ignore) {
					}

				throw e;
			}
		}

		private void throwPropertyInfoException(Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			throw new RuntimeException("Unable to get info about property \"" + propertyName + "\"\ncaused by: \n"
					+ sw.toString());
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = prime + ((parentPropertyHolder == null) ? 0 : parentPropertyHolder.hashCode());
			return prime * result + propertyName.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PropertyHolder other = (PropertyHolder) obj;
			if (parentPropertyHolder == null) {
				if (other.parentPropertyHolder != null)
					return false;
			} else if (!parentPropertyHolder.equals(other.parentPropertyHolder))
				return false;

			return propertyName.equals(other.propertyName);
		}

	}
}
