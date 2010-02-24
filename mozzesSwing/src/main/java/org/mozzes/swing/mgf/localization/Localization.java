package org.mozzes.swing.mgf.localization;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.mozzes.validation.ValidationFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Localization {
	private static final String BUNDLE_NAME = "mgflocalization";
	private static final String DEFAULT_BUNDLE_NAME_PREFIX = "default";
	private static Localization instance;

	private static final Logger logger = LoggerFactory.getLogger(Localization.class);

	private final Locale languageLocale;

	private final ResourceBundle fallback;
	private final ResourceBundle framework;
	private final ResourceBundle overrides;

	private Localization(Locale locale) {
		this.languageLocale = locale;

		fallback = getBundle(getDefaultBundleName());
		if (fallback == null)
			throw new IllegalStateException("Fallback resource(default_mgflocalization) must exist!");
		framework = getBundle(getDefaultBundleName(), locale);
		overrides = getBundle(getApplicationBundleName(), locale);
		if (framework == null && overrides == null) {
			logger.warn(String.format("Requested language(%s) for MGF does not exist, " +
					"default language(US English) will be in use!", locale.getLanguage()));
		}

		ValidationFactory.initialize(locale);
	}

	public static void initialize() {
		initialize(Locale.getDefault());
	}

	public static void initialize(String languageCode) {
		if (languageCode == null || languageCode.isEmpty())
			throw new IllegalArgumentException("Language code must be provided!");
		Locale locale = new Locale(languageCode);
		initialize(locale);
	}

	public static void initialize(Locale locale) {
		instance = new Localization(locale);
	}

	private static Localization getInstance() {
		if (instance == null)
			initialize();
		return instance;
	}

	public static String getValue(LocalizationKey key) {
		return Localization.getInstance().getValueNonStatic(key);
	}

	public static String getValue(LocalizationKey key, Object... params) {
		if (key == null) {
			return "null";
		}
		String msg = getValue(key);
		if (params == null) {
			return msg;
		}
		return MessageFormat.format(msg, params);
	}

	public static String getLanguageCode() {
		return getInstance().languageLocale.getLanguage();
	}

	public static Locale getLocale() {
		return getInstance().languageLocale;
	}

	private ResourceBundle getBundle(String bundleName, Locale languageLocale) {
		ResourceBundle bundle;
		try {
			bundle = ResourceBundle.getBundle(bundleName, languageLocale);
			if (!bundle.getLocale().getLanguage().equals(languageLocale.getLanguage()))
				bundle = null;
		} catch (MissingResourceException e) {
			bundle = null;
		}
		return bundle;
	}

	private ResourceBundle getBundle(String bundleName) {
		ResourceBundle bundle;
		try {
			bundle = ResourceBundle.getBundle(bundleName);
		} catch (MissingResourceException e) {
			bundle = null;
	}
		return bundle;
	}

	private static String getDefaultBundleName() {
		return DEFAULT_BUNDLE_NAME_PREFIX + "_" + BUNDLE_NAME;
	}

	private static String getApplicationBundleName() {
		return BUNDLE_NAME;
	}

	private String getValueNonStatic(LocalizationKey key) {
		if (key == null) {
			return "NULL";
		}

		String keyString = key.getKey();
		String value = "NULL";
		try {
			value = overrides.getString(keyString);
			// There is no override for the specified key, try using default key from same language
		} catch (Exception e) {
			try {
				value = framework.getString(keyString);
				// There is no language or specified key in that language, use key from default languages
			} catch (Exception e1) {
				value = fallback.getString(keyString);
			}
		}

		return value;
	}
		}
