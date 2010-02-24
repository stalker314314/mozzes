package org.mozzes.swing.mgf.binding.binders;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;

import org.mozzes.swing.mgf.binding.AbstractPropagator;
import org.mozzes.swing.mgf.binding.BindingHandler;


public class ComboBoxBinder<BeanType, ValueType> extends BindingHandler<BeanType, ValueType, JComboBox> {
	private final ToSourceBinder toSourceBinder = new ToSourceBinder();

	public ComboBoxBinder() {
		super(JComboBox.class);
		setToSourcePropagator(toSourceBinder);
	}

	@Override
	protected void bind() {
		getComponent().addItemListener(toSourceBinder);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected ValueType getComponentValue() {
		return (ValueType) getComponent().getSelectedItem();
	}

	@Override
	protected void setComponentValue(ValueType value) {
		getComponent().setSelectedItem(value);
	}

	@Override
	protected void setEditable(boolean editable) {
		getComponent().setEnabled(editable);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<ValueType> getComponentValueType() {
		return (Class<ValueType>) getField().getFieldType();
	}

	private class ToSourceBinder extends AbstractPropagator implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (!isEnabled())
				return;
			this.disable();
			promoteChangeToSource();
			this.enable();
			
			triggerValidation();
		}
	}
}
