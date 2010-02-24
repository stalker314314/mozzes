package org.mozzes.swing.component.convinient;

import javax.swing.JLabel;

/**
 * labela koju treba koristiti kada oznacavamo da je neko polje/podatak obavezno...
 * 
 * @author nikolad
 * 
 */
public class RequiredFieldLabel extends JLabel {

	private static final long serialVersionUID = -8811770108081120103L;

	private static final String REQUIRED = "*";

	public RequiredFieldLabel(JLabel label) {
		super(label.getText());
	}

	public RequiredFieldLabel(String text) {
		super(text);
	}

	@Override
	public void setText(String text) {
		super.setText(text.trim() + REQUIRED);
	}
}
