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
