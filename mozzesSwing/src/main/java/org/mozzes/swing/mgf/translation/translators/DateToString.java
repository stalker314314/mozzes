package org.mozzes.swing.mgf.translation.translators;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.mozzes.swing.mgf.localization.Localization;
import org.mozzes.swing.mgf.localization.LocalizationKey;
import org.mozzes.swing.mgf.translation.AbstractTranslator;
import org.mozzes.swing.mgf.translation.TranslationException;


public class DateToString extends AbstractTranslator<Date, String> {
	private static final DateFormat defaultFormat = new SimpleDateFormat("dd.MM.yyyy.");
	private final DateFormat format;

	public DateToString() {
		this.format = null;
	}

	public DateToString(DateFormat format) {
		this.format = format;
	}

	@Override
	public Class<Date> getFromClass() {
		return Date.class;
	}

	@Override
	public Class<String> getToClass() {
		return String.class;
	}

	@Override
	public Date translateFrom(String object) throws TranslationException {
		try {
			DateFormat df = format == null ? defaultFormat : format;
			return df.parse(object);
		} catch (ParseException e) {
			throw new TranslationException(Localization.getValue(
					LocalizationKey.INVALID_DATE_FORMAT_MESSAGE), e);
		}
	}

	/**
	 * @throws TranslationException if date in not well formated
	 */
	@Override
	public String translateTo(Date object) throws TranslationException {
		DateFormat df = format == null ? defaultFormat : format;
		return object == null ? "" : df.format(object);
	}

}
