package org.mozzes.swing.mgf.binding.binders;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import org.jdesktop.swingx.JXDatePicker;
import org.mozzes.swing.mgf.binding.AbstractPropagator;
import org.mozzes.swing.mgf.binding.BindingHandler;


public class JXDatePickerBinder<T> extends BindingHandler<T, Date, JXDatePicker> {
	private final ToSourceBinder toSourceBinder = new ToSourceBinder();

	public JXDatePickerBinder() {
		super(JXDatePicker.class);
	}

	@Override
	protected void bind() {
		getComponent().addActionListener(toSourceBinder);
	}

	@Override
	protected Date getComponentValue() {
		return getComponent().getDate();
	}

	@Override
	public Class<Date> getComponentValueType() {
		return Date.class;
	}

	@Override
	protected void setComponentValue(Date value) {
		getComponent().setDate(value);
	}

	@Override
	protected void setEditable(boolean editable) {
		boolean editorEditable = getComponent().getEditor().isEditable();
		getComponent().setEditable(editable);
		if(editable)
			getComponent().getEditor().setEditable(editorEditable);
		// getComponent().setEnabled(editable);
	}

	private class ToSourceBinder extends AbstractPropagator implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isEnabled())
				return;
			promoteChangeToSource();
		}
	}
}
