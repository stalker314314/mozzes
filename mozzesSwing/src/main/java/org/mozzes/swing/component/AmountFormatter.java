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
package org.mozzes.swing.component;

import java.math.BigDecimal;

/**
 * Vrsi zaokruzivanje prosledjenih iznosa na odgovarajuci broj decimala.
 * Objekat nije ThreadSafe.
 * @author Borko Grecic
 */
public class AmountFormatter {

	private int numOfDigits;
	
	
	/**
	 * Vraca instancu {@link AmountFormatter} objekta koji prosledjene iznose odseca na dve decimale
	 * @return {@link AmountFormatter}
	 */
	public static final AmountFormatter getTwoDigitsInstance() {
		return new AmountFormatter(Integer.valueOf(2));
	}
	
	/**
	 * Vraca instancu {@link AmountFormatter} objekta koji prosledjene iznose odseca na cetiri decimale
	 * @return {@link AmountFormatter}
	 */
	public static final AmountFormatter getFourDigitsInstance() {
		return new AmountFormatter(Integer.valueOf(4));
	}
	
	private AmountFormatter(final Integer num) {
		if(num == null)
			throw new IllegalArgumentException("Prosledjeni broj decimala  ne sme biti null.");
		this.numOfDigits = num;
	}

	/**
	 * U slucaju da je prosledjeni objekat instanca {@link Double} klase, vrsi se formatiranje (odsecanje na odredjeni broj decimala).
	 * U suprotnom vraca se prosledjena vrednost
	 * @param cellEditorValue {@link Object} iznos koji je potrebno formatirati
	 * @return Formatiran iznos ili prosledjeni parametar
	 */
	public Object format(final Object cellEditorValue) {
		return (cellEditorValue instanceof Double) ? truncate((Double)cellEditorValue) : cellEditorValue;
	}
	
	/**
	 * Odesacanje 
	 */
	public Double truncate(Double x) {   
	    BigDecimal bd = new BigDecimal(x.toString());   // ! - izuzetno je vazno da se koristi ovaj konstruktor sa stringom !  
	    bd = bd.setScale(numOfDigits , BigDecimal.ROUND_DOWN);
	    return Double.valueOf(bd.doubleValue());
	   }
}