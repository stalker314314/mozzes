package org.mozzes.swing.mgf.binding.binders;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTextArea;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

import org.mozzes.swing.mgf.binding.AbstractPropagator;
import org.mozzes.swing.mgf.binding.BindingHandler;


public class TextAreaBinder<T> extends BindingHandler<T, String, JTextArea> {
	private final ToSourceBinder toSourceBinder = new ToSourceBinder();
	private ValidationFocusListener updateAndValidateOnLostFocus = new ValidationFocusListener();

	public TextAreaBinder() {
		super(JTextArea.class);
		setToSourcePropagator(toSourceBinder);
	}

	@Override
	protected void bind() {
		getComponent().addFocusListener(updateAndValidateOnLostFocus);
		getComponent().getDocument().addUndoableEditListener(toSourceBinder);
	}

	@Override
	protected String getComponentValue() {
		return getComponent().getText();
	}

	@Override
	protected void setComponentValue(String value) {
		getComponent().setText(value);
	}

	@Override
	protected void setEditable(boolean editable) {
		getComponent().setEditable(editable);
	}

	@Override
	public Class<String> getComponentValueType() {
		return String.class;
	}

	private class ToSourceBinder extends AbstractPropagator implements UndoableEditListener {
		@Override
		public void undoableEditHappened(UndoableEditEvent e) {
			if (!isEnabled())
				return;
			promoteChangeToSource();
		}
	}

	private class ValidationFocusListener extends FocusAdapter {
		@Override
		public void focusLost(FocusEvent e) {
			promoteChangeFromSource();
			triggerValidation();
}
	}
}
