package org.mozzes.swing.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Pomocne funkcije.
 * 
 * @author neda
 */
public class CodeUtils {
	
	/**
	 * Proverava da li string predstavlja Double vrednost.
	 */
	public static boolean checkDouble(String s) {
		boolean ret = true;
		try {
			Double.parseDouble(s);
		} catch (Exception ex) {
			ret = false;
		}
		return ret;
	}
	
	/**
	 * Proverava da li string predstavlja Integer vrednost.
	 */
	public static boolean checkInteger(String s) {
		boolean ret = true;
		try {
			Integer.parseInt(s);
		} catch (Exception ex) {
			ret = false;
		}
		return ret;
	}
	
	/**
	 * [FIN-77] [nenadl] Vracja string reprezentaciju prosledjenog broja sa proizovljnim brojem decimala i opcionim
	 * razmakom izmedju hiljada.
	 * 
	 * @param delimeter - string koji razdvaja hiljade, ako je null onda se ne razdvajaju.
	 */
	public static String format(Number value, int noOfDecimals, String delimeter) {

		DecimalFormat decimalFormater = (DecimalFormat) NumberFormat
				.getInstance(Locale.US);

		StringBuilder pattern = new StringBuilder("0");
		if (noOfDecimals > 0) {
			pattern.append(".");
			for (int i = 0; i < noOfDecimals; i++) {
				pattern.append("0");
			}
		}

		decimalFormater.applyPattern(pattern.toString());
		String output = decimalFormater.format(value);
		
		//-0 prebacujemo u 0, vatamo exception u sluchaju da je neki chudan broj, tipa beskonachno
		try {
			if (Double.valueOf(output).doubleValue() == 0 && output.startsWith("-")) {
				output = output.substring(1, output.length());
			}
		} catch (NumberFormatException e) {
		}
			
		if (delimeter != null) {
			output = groupDigits(output, delimeter);
		}

		return output;
	}

	/**
	 * [FIN-77] [nenadl] Vracja string reprezentaciju prosledjenog broja sa proizovljnim brojem decimala i opcionim
	 * razmakom izmedju hiljada.
	 * 
	 * @param delimeter string koji razdvaja hiljade, ako je null onda se ne razdvajaju.
	 */
	public static String formatWithOptionalDecimals(Number value,
			int noOfDecimals, String delimeter) {

		DecimalFormat decimalFormater = (DecimalFormat) NumberFormat
				.getInstance(Locale.US);

		StringBuilder pattern = new StringBuilder("0");
		if (noOfDecimals > 0) {
			pattern.append(".");
			for (int i = 0; i < noOfDecimals; i++) {
				pattern.append("#");
			}
		}

		decimalFormater.applyPattern(pattern.toString());
		String output = decimalFormater.format(value);
		
		//-0 prebacujemo u 0
		if (Double.valueOf(output).doubleValue() == 0 && output.startsWith("-")) {
			output = output.substring(1, output.length());
		}
		
		if (delimeter != null) {
			output = groupDigits(output, delimeter);
		}

		return output;
	}
	
	/**
	 * [FIN-77] [nenadl] Ubacuje spaceove u decimalni ili ceo broj izmedju hiljadarki.
	 * 
	 * @param aNumber - broj u obliku stringa.
	 * @param delimeter - string koji razdvaja hiljade.
	 * @return - formatirani broj u obliku stringa.
	 */
	private static String groupDigits(String aNumber, String delimeter) {
		String number = aNumber;
		int decimalPlaces = 0;
		int pointPosition = number.indexOf(".");
		
		if(pointPosition > 0) {
			// Uracunava se i tacka
			decimalPlaces = number.length() - pointPosition;
		}
				
		// Preskacu se negrupisane cifre na pocetku, poslednja grupa, decimalne cifre
		int delimeterLength = delimeter.length();
		for (int i = (number.length() - decimalPlaces)%3; i <= number.length() - 3 - decimalPlaces; i+=3) {
			
			// Na prvo mesto ne ide space
			if (i == 0) {
				continue;
			}
			
			// Ubacivanje razdvajacha usred stringa 
			number = number.substring(0, i) + delimeter + number.substring(i, number.length());
			i += delimeterLength;
		}
		
		return number;
	}

	/**
	 * Poredi da li su dva objekta ista. 
	 * 
	 * @return <code>true</code> ako su oba objekta <code>null</code>, ili ako su equals(). Inace vraca false.
	 */
	public static boolean equalsWithNull(Object o1, Object o2) {
		
		if(o1 == null && o2 == null) 
			return true;
		
		if(o1 != null)
			return o1.equals(o2);
		
		return false;
	}
}
