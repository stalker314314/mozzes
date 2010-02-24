package org.mozzes.swing.mgf.translation.translators;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.mozzes.swing.mgf.translation.AbstractTranslator;
import org.mozzes.swing.mgf.translation.TranslationException;


public class TimestampToString extends AbstractTranslator<Timestamp, String> {
	private static final DateFormat defaultFormat = new SimpleDateFormat("dd.MM.yyyy. HH:mm");
	private final DateFormat format;

	public TimestampToString() {
		this.format = null;
	}

	public TimestampToString(DateFormat format) {
		this.format = format;
	}

	@Override
	public Class<Timestamp> getFromClass() {
		return Timestamp.class;
	}

	@Override
	public Class<String> getToClass() {
		return String.class;
	}

	@Override
	public Timestamp translateFrom(String object) throws TranslationException {
		try {
			DateFormat df = format == null ? defaultFormat : format;
			return (Timestamp) df.parse(object);
		} catch (ParseException e) {
			throw new TranslationException(e.getMessage(), e);
		}
	}

	/**
	 * @throws TranslationException if date in not well formated
	 */
	@Override
	public String translateTo(Timestamp object) throws TranslationException {
		DateFormat df = format == null ? defaultFormat : format;
		return object == null ? "" : df.format(object);
	}
}
