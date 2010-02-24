package org.mozzes.swing.mgf.binding.binders;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.mozzes.swing.mgf.binding.BindingHandler;
import org.mozzes.utils.reflection.ReflectionUtils;


public class LabelBinder<T> extends BindingHandler<T, String, JLabel> {
	public LabelBinder() {
		super(JLabel.class);
	}

	@Override
	protected void bind() {
		alignRightIfNumber();
	}

	private void alignRightIfNumber() {
		Class<?> fieldType = getField().getFieldType();
		fieldType = fieldType.isPrimitive() ? ReflectionUtils.resolvePrimitiveType(fieldType) : fieldType;
		if (Number.class.isAssignableFrom(fieldType))
			getComponent().setHorizontalAlignment(SwingConstants.RIGHT);
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
	}

	@Override
	public Class<String> getComponentValueType() {
		return String.class;
	}
}
