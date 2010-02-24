package org.mozzes.swing.mgf.translation;

import java.util.ArrayList;
import java.util.List;

import org.mozzes.swing.mgf.translation.translators.DateToString;
import org.mozzes.swing.mgf.translation.translators.DoubleToString;
import org.mozzes.swing.mgf.translation.translators.IntegerToString;
import org.mozzes.swing.mgf.translation.translators.TimestampToString;


/**
 * Contains the default translators set by MGF Framework
 * 
 * @author milos
 */
public class DefaultTranslators {
	private DefaultTranslators() {
	}

	/**
	 * @return List of default translators to be set by MGF Framework
	 */
	public static List<Translator<?, ?>> getDefaultTranslators() {
		List<Translator<?, ?>> translators = new ArrayList<Translator<?, ?>>();

		translators.add(new IntegerToString());
		translators.add(new DoubleToString());
		translators.add(new DateToString());
		translators.add(new TimestampToString());

		return translators;
	}
}
