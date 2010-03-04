/*
 * Copyright 2010 Mozzart
 *
 *
 * This file is part of mozzes.
 *
 * mozzes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mozzes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mozzes.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.mozzes.swing.mgf.binding;

import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.validation.ConstraintViolation;
import javax.validation.groups.Default;

import org.mozzes.swing.mgf.datamodel.Field;
import org.mozzes.swing.mgf.datamodel.events.field.FieldEvent;
import org.mozzes.swing.mgf.datamodel.events.field.FieldEventListener;
import org.mozzes.swing.mgf.datamodel.fields.PropertyField;
import org.mozzes.swing.mgf.datasource.BeanDataSource;
import org.mozzes.swing.mgf.datasource.DataSource;
import org.mozzes.swing.mgf.datasource.events.DataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.bean.BeanDataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.bean.BeanDataSourceEventListener;
import org.mozzes.swing.mgf.decoration.Decorator;
import org.mozzes.swing.mgf.decoration.Templates;
import org.mozzes.swing.mgf.decoration.decorators.MessageDecorator;
import org.mozzes.swing.mgf.translation.TranslationException;
import org.mozzes.swing.mgf.translation.Translator;
import org.mozzes.swing.mgf.translation.TranslatorRepository;
import org.mozzes.swing.mgf.validation.Realtime;
import org.mozzes.validation.ValidationFactory;


/**
 * Abstract base for all classes that describe how a component can be bound to a {@link Field field} value of a bean <br>
 * <b>NOTICE: All subclasses must have public parameterless constructor!</b>
 * 
 * @author milos
 * 
 * @param <BeanType> Type of a bean the component is bound to
 * @param <ComponentValueType> Type that the component natively returns as its value
 * @param <ComponentType> Type of a component which the specific handler subclass is handling
 */
public abstract class BindingHandler<BeanType, ComponentValueType, ComponentType extends JComponent> {
	private final Class<ComponentType> componentClass;
	private BeanDataSource<BeanType> source;
	private Field<BeanType, Object> field;
	private ComponentType component;
	private Translator<ComponentValueType, ?> translator;
	private final FromSourcePropagator fromSourcePropagator = new FromSourcePropagator();
	private Propagator toSourcePropagator;

	private Set<ConstraintViolation<BeanType>> violations = null;
	private Decorator validityDecorator;
	private MessageDecorator messageDecorator;
	private MessageControlListener messageControlFocus = new MessageControlListener();

	/**
	 * @param componentClass Class of a component which the specific handler subclass is handling
	 */
	protected BindingHandler(Class<ComponentType> componentClass) {
		if (componentClass == null)
			throw new IllegalArgumentException("ComponentClass cannot be null!");
		this.componentClass = componentClass;
	}

	/**
	 * @return Class of a component which the specific handler subclass is handling
	 */
	public Class<ComponentType> getComponentClass() {
		return componentClass;
	}

	/**
	 * @return <b>true</b> if the translation is necessary, <b>false</b> otherwise
	 */
	private boolean isNonNeutralTranslatorProvided() {
		if (translator == null)
			return false;
		return !TranslatorRepository.getNeutral(getComponentValueType()).equals(translator);
	}

	/**
	 * Does initialization, final setups and other boiler-plate code required for binding, also calls
	 * {@link BindingHandler#bind()} to perform the actual binding<br>
	 * NOTE: This method is called by the {@link Binder} class, but the method doing the real work is
	 * {@link BindingHandler#bind()}
	 * 
	 * @param <T> Type of the value represented by the specified {@link Field field}
	 * @param component GUI {@link JComponent} which should be bound
	 * @param source {@link BeanDataSource Source} which will provide a bean
	 * @param field {@link Field} which will be used to get and set some value to the bound bean
	 * @param translator {@link Translator} from value provided by component to <b>T</b>
	 */
	@SuppressWarnings("unchecked")
	public <T> void bind(Component component, BeanDataSource<BeanType> source, Field<BeanType, T> field,
			Translator<ComponentValueType, T> translator) {

		if (!componentClass.isAssignableFrom(component.getClass()))
			throw new IllegalArgumentException("Component must be of specified class!");

		setSource(source);
		this.setField((Field<BeanType, Object>) field);
		setComponent((ComponentType) component);
		setTranslator(translator);
		refreshEditability();

		bind();

		Class<ComponentType> componentClass = (Class<ComponentType>) component.getClass();
		validityDecorator = Templates.getTemplateFor(componentClass);
		messageDecorator = Templates.getMessageDecoratorFor(componentClass);

		component.addFocusListener(messageControlFocus);
		component.addComponentListener(messageControlFocus);

		promoteChangeFromSource();
	}

	/**
	 * Checks if the provided translator is valid(able to convert between the ComponentValueType and FieldValueType)
	 */
	private void checkTranslator() {
		if (!isNonNeutralTranslatorProvided()) {
			if (!field.getFieldType().isAssignableFrom(getComponentValueType()))
				throw new IllegalArgumentException(String.format(
						"Translator from %s to %s must be provided!\nField: %s",
						getComponentValueType().getSimpleName(), field.getFieldType().getSimpleName(),
						field.toString()));
		} else {
			if (!field.getFieldType().isAssignableFrom(translator.getToClass()) ||
					!getComponentValueType().isAssignableFrom(translator.getFromClass()))
				throw new IllegalArgumentException(String.format(
						"Provided translator (%s to %s) is invalid!\n" +
						"Translator from %s to %s must be provided!\nField: %s",
						translator.getFromClass().getSimpleName(), translator.getToClass().getSimpleName(),
						getComponentValueType().getSimpleName(), field.getFieldType().getSimpleName(),
						field.toString()));
		}
	}

	/**
	 * Implemented by subclasses to do the actual binding(ie: add appropriate listeners to component)
	 */
	protected abstract void bind();

	/**
	 * Sets the passed {@link BeanDataSource source} as the source for this BindingHandler instance
	 */
	private void setSource(BeanDataSource<BeanType> source) {
		if (this.source != null)
			this.source.removeEventListener(fromSourcePropagator);
		this.source = source;
		this.source.addEventListener(fromSourcePropagator);
	}

	/**
	 * @return {@link BeanDataSource Source} for which this instance of {@link BindingHandler} is bound
	 */
	public BeanDataSource<BeanType> getSource() {
		return source;
	}

	/**
	 * Implemented by subclasses
	 * 
	 * @return Class of the value that the component natively returns as its value
	 */
	public abstract Class<ComponentValueType> getComponentValueType();

	/**
	 * Sets the passed {@link Field field} as the bound field for this BindingHandler instance
	 */
	private void setField(Field<BeanType, Object> field) {
		this.field = field;
		field.addEventListener(fromSourcePropagator);
	}

	/**
	 * @return {@link Field} for which this instance of {@link BindingHandler} is bound
	 */
	public Field<BeanType, ?> getField() {
		return field;
	}

	/**
	 * Sets the passed <i>component</i> as the bound bound component for this BindingHandler instance
	 */
	private void setComponent(ComponentType component) {
		this.component = component;
	}

	/**
	 * @return Component of type <i>ComponentType</i> for which this instance of {@link BindingHandler} is bound
	 */
	public ComponentType getComponent() {
		return component;
	}

	/**
	 * Sets the provided translator, or the default one if the <i>translator</i> parameter is <b>null</b>!
	 * 
	 * @param translator {@link Translator} that will be used to convert the value provided by the bound component to a
	 *            value required by the bound {@link Field field} and backwards
	 */
	private void setTranslator(Translator<ComponentValueType, ?> translator) {
		Translator<ComponentValueType, ?> real = translator;
		if (real == null) {
			if (field.getFieldType().isAssignableFrom(getComponentValueType())) {
				real = TranslatorRepository.getNeutral(getComponentValueType());
			} else {
				real = TranslatorRepository.getDefault(getComponentValueType(), field.getFieldType());
			}
		}
		this.translator = real;
		checkTranslator();
	}

	/**
	 * @return {@link Translator} which is used to convert the value provided by the bound component to a value required
	 *         by the bound {@link Field field} and backwards
	 */
	public Translator<ComponentValueType, ?> getTranslator() {
		return translator;
	}

	/**
	 * Sets the {@link Propagator} which propagates changes from the bound component to the bound source
	 */
	protected void setToSourcePropagator(Propagator toSource) {
		this.toSourcePropagator = toSource;
	}

	/**
	 * @return {@link Propagator} which propagates changes from the bound component to the bound source
	 */
	protected Propagator getToSourcePropagator() {
		return toSourcePropagator;
	}

	// /**
	// * Sets the {@link Propagator} which propagates changes from the bound source to the bound component
	// */
	// protected void setFromSourcePropagator(Propagator fromSource) {
	// this.fromSourcePropagator = fromSource;
	// }

	/**
	 * @return {@link Propagator} which propagates changes from the bound source to the bound component
	 */
	protected Propagator getFromSourcePropagator() {
		return fromSourcePropagator;
	}

	protected final void triggerValidation() {
		triggerValidation(Realtime.class, Default.class);
	}

	private final void triggerRealtimeValidation() {
		triggerValidation(Realtime.class);
	}

	private final void triggerValidation(Class<?>... groups) {
		if (source.getData() == null)
			return;

		if (!(field instanceof PropertyField<?, ?>))
			return;
		clearDecorators();

		PropertyField<BeanType, Object> propField = (PropertyField<BeanType, Object>) field;
		Set<ConstraintViolation<BeanType>> violations =
				ValidationFactory.getValidator().validateProperty(source.getData(), propField.getProperty(), groups);

		this.violations = violations;
		if (violations == null || violations.isEmpty())
			return;

		showValidationExceptionMessage(violations);
	}

	/**
	 * This should be called by subclasses(in {@link Propagator ToSourcePropagator}) when a value in the bound component
	 * changes in order to promote the change to the bean contained by the bound source
	 */
	protected final void promoteChangeToSource() {
		clearDecorators();

		beforeSetingDataToSource();
		if (fromSourcePropagator != null)
			fromSourcePropagator.disable();
		try {
			updateSourceFromComponent();
			if (!isValid())
				triggerValidation();
			else
			triggerRealtimeValidation();
		} catch (TranslationException e) {
			promoteChangeFromSource();
			showTranslationExceptionMessage(e);
		}
		if (fromSourcePropagator != null)
			fromSourcePropagator.enable();
		afterSetingDataToSource();
	}

	/**
	 * This is called by {@link Propagator FromSourcePropagator} when a value of the bound {@link Field} in the bean
	 * contained by the bound source changes in order to propagate the change to the bound component <br>
	 * Note: It can also be called in whichever situation a subclass wants to refresh the value of a component
	 */
	protected final void promoteChangeFromSource() {
		refreshEditability();
		if (isSynchronized())
			return;

		clearDecorators();

		beforeSetingValueToComponent();
		if (toSourcePropagator != null)
			toSourcePropagator.disable();
		try {
			field.fireValueChanged(source.getData());
			updateComponentFromSource();
			if (!isValid())
				triggerValidation();
			else
				triggerRealtimeValidation();

		} catch (TranslationException e) {
			showTranslationExceptionMessage(e);
		}
		if (toSourcePropagator != null)
			toSourcePropagator.enable();
		afterSetingValueToComponent();
	}

	private boolean isSynchronized() {
		try {
			ComponentValueType val = getTranslatedValueFromSource();
			if (val == getComponentValue() ||
					(val != null && val.equals(getComponentValue())))
				return true;
		} catch (Exception ignore) {
			return false;
		}
		return false;
	}

	/**
	 * Sets the value of the {@link Field} for the bean to a value from {@link Component}
	 * 
	 * @throws TranslationException When a value from component fails to be translated by provided {@link Translator}
	 */
	@SuppressWarnings("unchecked")
	private void updateSourceFromComponent() throws TranslationException {
		Field<BeanType, Object> field = (Field<BeanType, Object>) getField();
		Object value = getTranslatedValueFromComponent();
		field.setValue(getSource().getData(), value);
		getSource().fireDataUpdatedEvent();
	}

	/**
	 * Sets the value of the component to value of the bound {@link Field} for a bean from the bound source
	 * 
	 * @throws TranslationException When a value of a {@link Field} for the bean fails to be translated by provided
	 *             {@link Translator}
	 */
	private void updateComponentFromSource() throws TranslationException {
		ComponentValueType translatedValueFromSource = getTranslatedValueFromSource();
		setComponentValue(translatedValueFromSource);
	}

	/**
	 * @return Value of the {@link Field} for the bean from source translated to a value requred by the compoenent
	 * @throws TranslationException When a value of a {@link Field} for the bean fails to be translated by provided
	 *             {@link Translator}
	 */
	@SuppressWarnings("unchecked")
	private ComponentValueType getTranslatedValueFromSource() throws TranslationException {
		Translator<ComponentValueType, Object> translator2 = (Translator<ComponentValueType, Object>) getTranslator();
		ComponentValueType translatedValueFromSource = translator2.translateFrom(
				getField().getValue(getSource().getData()));
		return translatedValueFromSource;
	}

	/**
	 * @return Value of the bound component translated to a value required by the bound {@link Field}
	 * @throws TranslationException When a value from component fails to be translated by provided {@link Translator}
	 */
	private Object getTranslatedValueFromComponent() throws TranslationException {
		return getTranslator().translateTo(getComponentValue());
	}

	/**
	 * Shows appropriate decorations on the bound component when some TranslationException occurs
	 * 
	 * @param e exception that occurred
	 */
	private void showTranslationExceptionMessage(TranslationException e) {
		showErrorMessages(e.getMessage());
	}

	/**
	 * Shows appropriate decorations on the bound component when some validation constraint is violated
	 * 
	 * @param violations Violations that were made
	 */
	private void showValidationExceptionMessage(Set<ConstraintViolation<BeanType>> violations) {
		List<String> messages = new ArrayList<String>();
		for (ConstraintViolation<BeanType> violation : violations) {
			messages.add(violation.getMessage());
	}
		showErrorMessages(messages.toArray(new String[messages.size()]));
	}

	// private String[] combineMessages(String... messages) {
	// String[] old = messageDecorator.getMessages();
	// String[] msgs = new String[0];
	// if (old != null)
	// msgs = Arrays.copyOf(old, old.length + messages.length);
	//
	// int j = 0;
	// for (int i = old == null ? 0 : old.length; i < msgs.length; i++) {
	// msgs[i] = messages[j++];
	// }
	// SortedSet<String> set = new TreeSet<String>();
	// set.addAll(Arrays.asList(msgs));
	// return set.toArray(new String[set.size()]);
	// }

	private void showErrorMessages(String... messages) {
		validityDecorator.decorate(component);
		messageDecorator.setMessages(messages);
		if (!component.hasFocus())
			return;
		messageDecorator.decorate(component);
	}

	private void clearDecorators() {
		validityDecorator.clear(component);
		messageDecorator.clear(component);
	}

	private void resetValidation() {
		violations = null;
		clearDecorators();
	}

	private boolean isValid() {
		return (violations == null || violations.isEmpty());
	}

	/**
	 * Used to set the editable/enabled state of the bound component based on the {@link Field#isEditableFor(Object)}
	 * for the current object contained by the bound source
	 */
	private void refreshEditability() {
		setEditable(getField().isEditableFor(getSource().getData()));
	}

	/**
	 * Implemented by subclasses
	 * 
	 * @return Untranslated value that the component natively returns as its value
	 */
	protected abstract ComponentValueType getComponentValue();

	/**
	 * Implemented by subclasses<br>
	 * 
	 * Should be implemented to set the passed <i>value</i> to a component
	 * 
	 * @param value Value to be set
	 */
	protected abstract void setComponentValue(ComponentValueType value);

	/**
	 * Implemented by subclasses<br>
	 * 
	 * Should be implemented to set the editable/enabled state of the component according to the passed parameter
	 * 
	 * @param editable <b>true</b> if component should be editable, <b>false</b> otherwise
	 */
	protected abstract void setEditable(boolean editable);

	/**
	 * Overridden by subclasses if needed<br>
	 * 
	 * Called before a value is propagated to source
	 */
	protected void beforeSetingDataToSource() {
	}

	/**
	 * Overridden by subclasses if needed<br>
	 * 
	 * Called after a value has been propagated to source
	 */
	protected void afterSetingDataToSource() {
	}

	/**
	 * Overridden by subclasses if needed<br>
	 * 
	 * Called before a value is propagated from source to component
	 */
	protected void beforeSetingValueToComponent() {
	}

	/**
	 * Overridden by subclasses if needed<br>
	 * 
	 * Called after a value has been propagated from source to component
	 */
	protected void afterSetingValueToComponent() {
	}

	private class FromSourcePropagator extends AbstractPropagator
			implements BeanDataSourceEventListener<BeanType>, FieldEventListener<BeanType, Object> {

		@Override
		public void handleDataSourceEvent(BeanDataSource<BeanType> source, BeanDataSourceEvent<BeanType> event) {
			if (!isEnabled())
				return;
			promoteChangeFromSource();
		}

		@Override
		public void handleDataSourceEvent(DataSource<BeanType> source, DataSourceEvent<BeanType> event) {
			if (!isEnabled())
				return;
			promoteChangeFromSource();
		}

		@Override
		public void handleModelFieldEvent(Field<BeanType, Object> field, FieldEvent<BeanType, Object> event) {
			refreshEditability();
	}
}

	private class MessageControlListener extends ComponentAdapter implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {
			if (!isValid())
				messageDecorator.decorate(component);
}

		@Override
		public void focusLost(FocusEvent e) {
			messageDecorator.clear(component);
		}

		@Override
		public void componentHidden(ComponentEvent e) {
			resetValidation();
	}
}
}
