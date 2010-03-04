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
package org.mozzes.swing.mgf.datasource;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.mozzes.swing.mgf.datasource.events.DataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.DataSourceEventListener;
import org.mozzes.swing.mgf.datasource.events.SourceChangedEvent;
import org.mozzes.swing.mgf.datasource.events.bean.DataUpdatedEvent;
import org.slf4j.LoggerFactory;


/**
 * Base class for all {@link DataSource} implementations
 * 
 * @author milos
 * 
 * @param <T> Type of the bean provided by the source
 */
public abstract class AbstractDataSource<T> implements DataSource<T>, Serializable {
	private static final long serialVersionUID = 15L;

	// private static final Logger logger = LoggerFactory.getLogger(AbstractDataSource.class);
	private final List<DataSourceEventListener<T>> listeners = new LinkedList<DataSourceEventListener<T>>();
	private DataSource<T> source;
	private T data;
	private final PropagationListener toSourcePropagation = new PropagationListener();
	private final PropagationListener fromSourcePropagation = new PropagationListener();
	private final List<Class<? extends DataSourceEvent<?>>> fires = new ArrayList<Class<? extends DataSourceEvent<?>>>();
	private final Class<T> dataType;

	/**
	 * Constructs the {@link AbstractDataSource}
	 */
	public AbstractDataSource(Class<T> dataType) {
		if (dataType == null)
			throw new IllegalArgumentException("Data type cannot be null!");
		this.dataType = dataType;
	}

	/**
	 * Constructs the {@link AbstractDataSource}
	 * 
	 * @param data Bean to be provided initially
	 */
	public AbstractDataSource(Class<T> dataType, T data) {
		if (dataType == null)
			throw new IllegalArgumentException("Data type cannot be null!");
		this.dataType = dataType;
		this.data = data;
	}

	/**
	 * Constructs the {@link AbstractDataSource}
	 * 
	 * @param dataSource Another {@link DataSource} to be used as source
	 */
	public AbstractDataSource(Class<T> dataType, DataSource<T> dataSource) {
		if (dataType == null)
			throw new IllegalArgumentException("Data type cannot be null!");
		this.dataType = dataType;
		bindTo(dataSource);
	}

	@Override
	public void addEventListener(DataSourceEventListener<T> listener) {
		if (listener == null)
			throw new IllegalArgumentException("Listener must not be null!");
		if (!listeners.contains(listener))
		listeners.add(listener);
	}

	@Override
	public void removeEventListener(DataSourceEventListener<T> listener) {
		listeners.remove(listener);
	}

	/**
	 * @return {@link DataSource} used to provide value of this {@link DataSource} (called chaining)
	 */
	protected DataSource<T> getBoundDataSource() {
		return source;
	}

	@Override
	public T getData() {
		if (getBoundDataSource() != null) {
			return getBoundDataSource().getData();
		}
		return data;
	}

	@Override
	public void setData(T data) {
		if (data == getData())
			return;
		if (source != null) {
			source = null;
			this.removeEventListener(toSourcePropagation);
			// source.setDataSource(data);
			// return;
		}
		T old = this.data;
		this.data = data;
		fireEvent(new SourceChangedEvent<T>(old, data));
	}

	@Override
	public void bindTo(DataSource<T> dataSource) {
		if (dataSource == this)
			throw new IllegalArgumentException();

		T oldData = getData();
		data = null;
		if (source != null) {
			source.removeEventListener(fromSourcePropagation);
			this.removeEventListener(toSourcePropagation);
		}
		listeners.add(0, toSourcePropagation);
		// this.addEventListener(toSourcePropagation);
		source = dataSource;
		source.addEventListener(fromSourcePropagation);
		fireEvent(new SourceChangedEvent<T>(oldData, source.getData()));
		return;
	}

	@Override
	public Class<T> getDataType() {
		return dataType;
	}

	@Override
	public void fireDataUpdatedEvent() {
		fireEvent(new DataUpdatedEvent<T>());
	}

	/**
	 * <em>For internal use ONLY, do not make this public  NEVER EVER.<br>
	 * Add a method which fires a specific event to the {@link DataSource} interface if you absolutely need it</em><br>
	 * Fires an event
	 * 
	 * @param event {@link DataSourceEvent} to fire
	 */
	protected void fireEvent(DataSourceEvent<T> event) {
		if (event == null)
			throw new IllegalArgumentException("Event must not be null!");

		// If the event is direct descendant of DataSourceEvent handle it without use of reflection
		if (event.getClass().getSuperclass().equals(DataSourceEvent.class)) {
			for (DataSourceEventListener<T> listener : listeners) {
				listener.handleDataSourceEvent(this, event);
			}
			return;
		}

		// If the event is not direct descendant of DataSourceEvent find the method
		for (DataSourceEventListener<T> listener : listeners) {
			try {
				Method handlerMethod = getHandlerMethod(listener, event);
				// if (handlerMethod == null)
				// continue;
				if (!handlerMethod.isAccessible())
					handlerMethod.setAccessible(true);
				handlerMethod.invoke(listener, this, event);
			} catch (IllegalArgumentException e) {
				// logger.error("", e);
			} catch (IllegalAccessException e) {
				// logger.error("", e);
			} catch (InvocationTargetException e) {
				// logger.error("", e);
				if (e.getTargetException() instanceof Error)
					throw (Error) e.getTargetException();
				if (e.getTargetException() instanceof RuntimeException) {
					throw (RuntimeException) e.getTargetException();
				} else { // this should never happen since handler methods should never declare checked exceptions
					throw new IllegalStateException("This should NEVER happen! Exception Message: "
							+ e.getTargetException().getMessage(), e.getTargetException());
				}
			}
		}
	}

	/**
	 * This method uses reflection to figure out which method of the listener to call<br>
	 * (This is done in order to have sugar syntax for many types of {@link DataSource data sources})
	 * 
	 * @param listener {@link DataSourceEventListener Listener} that should be notified about the event
	 * @param event {@link DataSourceEvent Event} that should be handled
	 * @return Reflection {@link Method method} from the listener that should handle the event
	 */
	private Method getHandlerMethod(DataSourceEventListener<T> listener, DataSourceEvent<T> event) {
		final String handlerMethodName = "handleDataSourceEvent";

		Method basicMethod = null;
		try {
			basicMethod = listener.getClass().getMethod(handlerMethodName, DataSource.class, DataSourceEvent.class);
		} catch (SecurityException notGonnaHappen) {
		} catch (NoSuchMethodException notGonnaHappen) {
		}

		List<Method> methods = new ArrayList<Method>();

		for (Method m : listener.getClass().getMethods()) {
			Class<?>[] params = m.getParameterTypes();

			// Enforce method name convention
			if (!m.getName().equals(handlerMethodName))
				continue;

			// Enforce parameters convention
			if (params.length != 2)
				continue;

			// Skip the default method
			if (params[0].equals(DataSource.class) && params[1].equals(DataSourceEvent.class))
				continue;

			// Actual check
			if (params[0].isAssignableFrom(this.getClass()) && params[1].isAssignableFrom(event.getClass())) {
				methods.add(m);
			}
		}
		if (methods.isEmpty())
			return basicMethod;

		Method method = methods.remove(0);
		for (Method m : methods) {
			if (method.getParameterTypes()[0].isAssignableFrom(m.getParameterTypes()[0]))
				method = m;
		}
		if (!event.getClass().getSuperclass().isAssignableFrom(method.getParameterTypes()[1]))
			return basicMethod;
		return method;
	}

	/**
	 * @param event Event to be checked
	 * @return true if this {@link DataSource} can fire this event, false otherwise
	 */
	@SuppressWarnings("unchecked")
	protected final boolean canFireEvent(DataSourceEvent<?> event) {
		if (event == null)
			throw new IllegalArgumentException("Event must not be null!");

		// Cache the fired events
		if (fires.isEmpty()) {
			for (Class<?> eventClass : getSupportedBaseEvents()) {
				if (DataSourceEvent.class.isAssignableFrom(eventClass))
					fires.add((Class<? extends DataSourceEvent<?>>) eventClass);
			}
		}

		for (Class<? extends DataSourceEvent<?>> eventClass : fires) {
			if (event.getClass().getSuperclass().equals(eventClass))
				return true;
		}
		return false;
	}

	/**
	 * Should return the list of base classes for supported events <b>(ALWAYS INCLUDING DataSourceEvent.class)</b>
	 * 
	 * Example for SelectionListDataSource:
	 * 
	 * <code>
	 * <pre>
	 * protected List&lt;Class&lt;?&gt;&gt; getFiredBaseEvents();
	 * {
	 * 	List&lt;Class&lt;?&gt;&gt; events = new ArrayList&lt;Class&lt;?&gt;&gt;();
	 * 	events.add(DataSourceEvent.class);
	 * 	events.add(ListDataSourceEvent.class);
	 * 	events.add(SelectionListDataSourceEvent.class);
	 * 	return events;
	 * }
	 * </pre>
	 * </code>
	 * 
	 * @return The list of base classes for supported events
	 */
	protected List<Class<?>> getSupportedBaseEvents() {
		List<Class<?>> supproted = new ArrayList<Class<?>>();
		supproted.add(DataSourceEvent.class);
		return supproted;
	}

	/**
	 * Helper class for returning some value from other thread
	 * 
	 * @author milos
	 * 
	 * @param <R> The type of result
	 */
	protected static abstract class RunnableWithResult<R> implements Runnable {
		private R result;

		@Override
		public void run() {
			result = runWithResult();
		}

		public abstract R runWithResult();
	}

	/**
	 * Runs the specified <i>runnable</i> in Swing's EventDispatchThread
	 * 
	 * @param <R> Type of the result
	 * @param runnable Defines a procedure that is going to be ran in EventDispatchThread
	 * @return The result of the runnable method
	 */
	protected <R> R runInEventDispatch(final RunnableWithResult<R> runnable) {
		if (SwingUtilities.isEventDispatchThread()) {
			runnable.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(runnable);
			} catch (InterruptedException e) {
				if (e.getCause() instanceof RuntimeException)
					throw (RuntimeException) e.getCause();
				else
					LoggerFactory.getLogger(this.getClass()).error(e.getMessage(), e);
			} catch (InvocationTargetException e) {
				if (e.getTargetException() instanceof RuntimeException)
					throw (RuntimeException) e.getTargetException();
				else
					throw new IllegalStateException(
							"This should never happen since there are no checked exceptions in addAll or fireEvent");
			}
		}
		return runnable.result;
	}

	/**
	 * This listener is used to propagate all the events if this {@link DataSource} uses another as its source
	 * 
	 * @author milos
	 */
	private class PropagationListener implements DataSourceEventListener<T> {
		private boolean enabled = true;

		@Override
		public void handleDataSourceEvent(DataSource<T> source, DataSourceEvent<T> event) {
			if (!enabled)
				return;
			if (AbstractDataSource.this.getBoundDataSource() == null)
				return;

			// Comparison using "==" not accidental
			PropagationListener otherHandler = (source == AbstractDataSource.this) ?
					fromSourcePropagation : toSourcePropagation;

			if (event instanceof SourceChangedEvent<?>) {
				if (source != AbstractDataSource.this) {// Not accidental, don't want to use equals
					otherHandler.disablePopagation();
					fireDataUpdatedEvent();
					otherHandler.disablePopagation();
				}
				return;
			}
			if (AbstractDataSource.this.getBoundDataSource() != null
					&& !(AbstractDataSource.this.getBoundDataSource() instanceof AbstractDataSource<?>))
				return;

			AbstractDataSource<T> propagateTo = (source == AbstractDataSource.this) ?
					(AbstractDataSource<T>) AbstractDataSource.this.getBoundDataSource()
					: AbstractDataSource.this;
			// DataSource<T> propagateFrom = (source == this) ? AbstractDataSource.this :
			// AbstractDataSource.this.source;

			otherHandler.disablePopagation();
			if (propagateTo.canFireEvent(event)) {
				event.setPropagated(true);
				propagateTo.fireEvent(event);
			} else {
				DataUpdatedEvent<T> dataUpd = new DataUpdatedEvent<T>();
				dataUpd.setPropagated(true);
				propagateTo.fireEvent(dataUpd);
				// propagateTo.fireDataUpdatedEvent();
			}
			otherHandler.enablePropagation();
		}

		public void disablePopagation() {
			this.enabled = false;
		}

		public void enablePropagation() {
			this.enabled = true;
		}
	};
}
