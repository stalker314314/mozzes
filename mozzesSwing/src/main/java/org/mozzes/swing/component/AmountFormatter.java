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