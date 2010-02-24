package org.mozzes.swing.mgf.binding.binders;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

import org.mozzes.swing.mgf.binding.AbstractPropagator;
import org.mozzes.swing.mgf.binding.BindingHandler;
import org.mozzes.utils.reflection.ReflectionUtils;


public class CheckBoxBinder<T> extends BindingHandler<T, Boolean, JCheckBox> {
	private final ToSourceBinder toSourceBinder = new ToSourceBinder();

	public CheckBoxBinder() {
		super(JCheckBox.class);
		setToSourcePropagator(toSourceBinder);
	}

	@Override
	protected void bind() {
		getComponent().addActionListener(toSourceBinder);
	}

	@Override
	protected Boolean getComponentValue() {
		return getComponent().isSelected();
	}

	@Override
	protected void setComponentValue(Boolean value) {
		Boolean nullResolved = ReflectionUtils.resolvePrimitiveNull(boolean.class, value);
		getComponent().setSelected(nullResolved);
	}

	@Override
	protected void setEditable(boolean editable) {
		getComponent().setEnabled(editable);
	}

	@Override
	public Class<Boolean> getComponentValueType() {
		return Boolean.class;
	}

	private class ToSourceBinder extends AbstractPropagator implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isEnabled())
				return;
			promoteChangeToSource();
			triggerValidation();
		}

	}
}
