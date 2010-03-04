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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.mozzes.swing.mgf.datamodel.Field;
import org.mozzes.swing.mgf.datamodel.fields.PropertyField;
import org.mozzes.swing.mgf.datasource.BeanDataSource;
import org.mozzes.swing.mgf.datasource.DataSource;
import org.mozzes.swing.mgf.translation.Translator;


/**
 * Allows clients to "bind" a component for some {@link PropertyField property}(or any other {@link Field}) of some bean
 * contained by a {@link DataSource}. <br>
 * Such a "binding" will keep the value of specified {@link Field} for a given bean and the value of a bound component
 * always in sync.
 * 
 * @author milos
 */
public abstract class Binder {
	// private static List<Class<BindingHandler<?, ?, ?>>> binders = null;
	private static List<Class<?>> binders = null;
	private static List<BindingHandler<?, ?, ?>> binderInstanceCache =
			new ArrayList<BindingHandler<?, ?, ?>>();

	private Binder() {
	}

	// initialization
	private static void initializeDefaultBinders() {
		if (binders != null)
			return;
		binders = new ArrayList<Class<?>>();
		for (Class<?> handler : DefaultBinders.getDefaultBinders()) {
			addBindingHandler(handler, false);
		}
	}

	// internal
	private static void cacheInstance(Class<?> binderClazz) {
		BindingHandler<?, ?, ?> instance = createInstace(binderClazz);
		binderInstanceCache.add(instance);
	}

	private static BindingHandler<?, ?, ?> createInstace(Class<?> binderClazz) {
		String s = "This will not happen when all binders are implemented properly!";
		try {
			return (BindingHandler<?, ?, ?>) binderClazz.newInstance();
		} catch (InstantiationException ignore) {
			throw new IllegalArgumentException(s);
		} catch (IllegalAccessException ignore) {
			throw new IllegalArgumentException(s);
		}
	}

	private static BindingHandler<?, ?, ?> getBinder(Component component) {
		List<BindingHandler<?, ?, ?>> eligableBinders = new ArrayList<BindingHandler<?, ?, ?>>();
		for (BindingHandler<?, ?, ?> binder : binderInstanceCache) {
			if (binder.getComponentClass().isAssignableFrom(component.getClass())) {
				eligableBinders.add(binder);
			}
		}
		if (eligableBinders.isEmpty())
			return null;

		BindingHandler<?, ?, ?> binder = eligableBinders.get(0);
		int size = eligableBinders.size();
		for (int i = 1; i < size; i++) {
			BindingHandler<?, ?, ?> tmp = eligableBinders.get(i);
			if (!tmp.getComponentClass().isAssignableFrom(binder.getComponentClass()))
				binder = tmp;
		}

		return createInstace(binder.getClass());
	}

	private static void checkAndApplyOverride(Class<?> hanlder, boolean override) {
		BindingHandler<?, ?, ?> handlerInstnace = createInstace(hanlder);
		List<BindingHandler<?, ?, ?>> toRemove = new ArrayList<BindingHandler<?, ?, ?>>();
		for (BindingHandler<?, ?, ?> instance : binderInstanceCache) {
			if (instance.getComponentClass() != handlerInstnace.getComponentClass())
				continue;

			if (!override) {
				throw new IllegalArgumentException(String.format(
						"Binder for specified component type(%s) is already registered!" +
						"Call BinderConfiguration.overrideBindingHandler to explicitly override it!",
						instance.getComponentClass().getSimpleName()));
			} else {
				toRemove.add(instance);
			}
		}
		for (BindingHandler<?, ?, ?> bindingHandler : toRemove) {
			binders.remove(bindingHandler.getClass());
			binderInstanceCache.remove(bindingHandler);
		}
	}

	// config
	/**
	 * Contributes a {@link BindingHandler} to framework
	 * 
	 * @param hanlder Class of the {@link BindingHandler handler} you want to contribute to framework
	 * @param override set to <b>true</b> if you want to allow this call to override any handlers that were set before
	 *            to handle the same type of component as the passed <i>handler</i>, <b>false</b> otherwise
	 */
	static void addBindingHandler(Class<?> hanlder, boolean override) {
		initializeDefaultBinders();

		if (hanlder == null)
			throw new IllegalArgumentException("Handler class cannot be null!");
		if (BindingHandler.class == hanlder || !BindingHandler.class.isAssignableFrom(hanlder))
			throw new IllegalArgumentException("Handler class must be a subclass of BindingHandler class!");
		if (binders.contains(hanlder))
			return;

		String constructorMessage = "BindingHandler must have public parameterless constructor!";
		try {
			hanlder.getConstructor();
		} catch (SecurityException ignore) {
			throw new IllegalArgumentException(constructorMessage);
		} catch (NoSuchMethodException ignore) {
			throw new IllegalArgumentException(constructorMessage);
		}

		checkAndApplyOverride(hanlder, override);

		binders.add(hanlder);
		cacheInstance(hanlder);
	}

	// interface
	/**
	 * Binds the <i>component</i> to a whole object in source in a read/write manner
	 * 
	 * @param <T> Type of the bean provided by <i>source</i>
	 * @param component GUI {@link JComponent} which should be bound
	 * @param source {@link BeanDataSource Source} which will provide a bean for binding
	 */
	public static <T> void bind(JComponent component, BeanDataSource<T> source) {
		bind(component, source, false);
	}

	/**
	 * Binds the <i>component</i> to a whole object in source<br>
	 * Allows you to set the read/write permissions
	 * 
	 * @param <T> Type of the bean provided by <i>source</i>
	 * @param component GUI {@link JComponent} which should be bound
	 * @param source {@link BeanDataSource Source} which will provide a bean for binding
	 * @param readOnly Set to <b>true</b> if you want to prevent the user from changing the value using the bound
	 *            component, <b>false</b> otherwise.
	 */
	public static <T> void bind(JComponent component, BeanDataSource<T> source, boolean readOnly) {
		bind(component, source, new ReadWriteWholeObjectField<T>(source, readOnly));
	}

	/**
	 * Binds the <i>component</i> to a whole object in source<br>
	 * Allows you to set the custom {@link Translator} that will be used(instead of the default one) to convert the
	 * value provided by component to a value required by the {@link BeanDataSource source} and backwards
	 * 
	 * @param <T> Type of the bean provided by <i>source</i>
	 * @param component GUI {@link JComponent} which should be bound
	 * @param source {@link BeanDataSource Source} which will provide a bean for binding
	 * @param translator A custom {@link Translator} from value provided by component to <b>T</b>
	 */
	public static <T> void bind(JComponent component, BeanDataSource<T> source, Translator<?, T> translator) {
		bind(component, source, new ReadWriteWholeObjectField<T>(source, false), translator);
	}

	/**
	 * Binds the <i>component</i> to a <i>{@link Field field}</i> of the bean provided by the <i>{@link BeanDataSource
	 * source}</i>
	 * 
	 * @param <T> Type of the bean provided by <i>source</i>
	 * @param <F> Type of the value represented by the specified {@link Field field}
	 * @param component GUI {@link JComponent} which should be bound
	 * @param source {@link BeanDataSource Source} which will provide a bean
	 * @param field {@link Field} which will be used to get and set some value to the bound bean
	 */
	public static <T, F> void bind(JComponent component, BeanDataSource<T> source, Field<T, F> field) {
		bind(component, source, field, null);
	}

	/**
	 * Binds the <i>component</i> to a <i>{@link Field field}</i> of the bean provided by the <i> {@link BeanDataSource
	 * source}</i>.<br>
	 * Allows you to set the custom {@link Translator} that will be used(instead of the default one) to convert the
	 * value provided by component to a value required by the specified {@link Field field} and backwards
	 * 
	 * @param <T> Type of the bean provided by <i>source</i>
	 * @param <F> Type of the value represented by the specified {@link Field field}
	 * @param component GUI {@link JComponent} which should be bound
	 * @param source {@link BeanDataSource Source} which will provide a bean
	 * @param field {@link Field} which will be used to get and set some value to the bound bean
	 * @param translator A custom {@link Translator} from value provided by component to <b>F</b>
	 */
	@SuppressWarnings("unchecked")
	public static <T, F> void bind(JComponent component, BeanDataSource<T> source, Field<T, F> field,
			Translator<?, F> translator) {
		initializeDefaultBinders();
		if (component == null || field == null || source == null)
			throw new IllegalArgumentException("Source, component or field cannot be null!");

		BindingHandler<T, Object, ?> binder = (BindingHandler<T, Object, ?>) getBinder(component);
		if (binder == null) {
			throw new UnsupportedOperationException("Passed component type is not supported!");
		}

		Field<T, Object> fieldCast = (Field<T, Object>) field;
		Translator<Object, Object> translatorCast = (Translator<Object, Object>) translator;

		binder.<Object> bind(component, source, fieldCast, translatorCast);
	}
}
