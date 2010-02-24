package org.mozzes.swing.mgf.datasource.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.mozzes.swing.mgf.datasource.DataSource;
import org.mozzes.swing.mgf.datasource.ListDataSource;
import org.mozzes.swing.mgf.datasource.events.list.ListDataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.list.ObjectReplacedEvent;
import org.mozzes.swing.mgf.datasource.events.list.ObjectsAddedEvent;
import org.mozzes.swing.mgf.datasource.events.list.ObjectsRemovedEvent;
import org.mozzes.swing.mgf.datasource.events.list.ObjectsUpdatedEvent;


/**
 * Default {@link ListDataSource} implementation
 * 
 * @author milos
 * 
 * @param <T> The type of the bean contained by the provided list
 */
public class DefaultListDataSource<T> extends DefaultBeanDataSource<List<T>> implements ListDataSource<T> {
	private static final long serialVersionUID = 15L;
	private final Class<T> elementType;

	/**
	 * Constructs the source with empty list as source
	 */
	@SuppressWarnings("unchecked")
	public DefaultListDataSource(Class<T> elementType) {
		super((Class<List<T>>) (Class<?>) List.class, new ArrayList<T>());
		if (elementType == null)
			throw new IllegalArgumentException("Element type cannot be null!");
		this.elementType = elementType;
	}

	/**
	 * @param data {@link List} that should be provided
	 */
	@SuppressWarnings("unchecked")
	public DefaultListDataSource(Class<T> elementType, List<T> data) {
		super((Class<List<T>>) (Class<?>) List.class, data);
		if (elementType == null)
			throw new IllegalArgumentException("Element type cannot be null!");
		this.elementType = elementType;
	}

	/**
	 * @param data Data that should be provided
	 */
	public DefaultListDataSource(Class<T> elementType, T[] data) {
		this(elementType, Arrays.asList(data));
	}

	/**
	 * @param dataSource Another source to use as the source for this one (chaining)
	 */
	public DefaultListDataSource(Class<T> elementType, DataSource<List<T>> dataSource) {
		super(dataSource);
		if (elementType == null)
			throw new IllegalArgumentException("Element type cannot be null!");
		this.elementType = elementType;
	}

	private ListDataSource<T> getListDataSource() {
		if (getBoundDataSource() == null)
			return null;
		try {
			return (ListDataSource<T>) getBoundDataSource();
		} catch (Exception e) {
			return null;
		}
	}

	public T get(int index) {
		if (getListDataSource() != null) {
			return getListDataSource().get(index);
		}
		return getData().get(index);
	}

	@Override
	public int getSize() {
		if (getListDataSource() != null) {
			return getListDataSource().getSize();
		}

		if (getData() == null)
			return 0;
		return getData().size();
	}

	@Override
	public T remove(final int index) {
		if (getListDataSource() != null) {
			return getListDataSource().remove(index);
		}
		return runInEventDispatch(new RunnableWithResult<T>() {
			@Override
			public T runWithResult() {
				T element = getData().remove(index);
				if (element != null) {
					Collection<T> elements = new ArrayList<T>();
					elements.add(element);
					fireEvent(new ObjectsRemovedEvent<T>(elements));
				}
				return element;
			}
		});
	}

	@Override
	public void clear() {
		if (getData() == null || getData().isEmpty())
			return;

		if (getListDataSource() != null) {
			getListDataSource().clear();
			return;
		}

		List<T> elements = new ArrayList<T>(getData());
		getData().clear();
		if (!elements.isEmpty())
			fireEvent(new ObjectsRemovedEvent<T>(elements));
	}

	@Override
	public T set(int index, T element) {
		if (getListDataSource() != null) {
			return getListDataSource().set(index, element);
		}

		if (element == getData().get(index)) // intentional comparison by reference
			return element;

		T replaced = getData().set(index, element);
		fireEvent(new ObjectReplacedEvent<T>(index, replaced, element));
		return replaced;
	}

	@Override
	public void add(T object) {
		add(wrapInList(object));
	}

	@Override
	public void add(T... elements) {
		add(Arrays.asList(elements));
	}

	@Override
	public void add(final Collection<T> elements) {
		if (getListDataSource() != null) {
			getListDataSource().add(elements);
			return;
		}

		runInEventDispatch(new RunnableWithResult<Void>() {
			@Override
			public Void runWithResult() {
				int size = getSize();
				final Collection<T> added = (getData() == elements) ?
						new ArrayList<T>(elements) : elements;
				getData().addAll(elements);
				fireEvent(new ObjectsAddedEvent<T>(size, added));
				return null;
			}
		});
	}

	@Override
	public void add(int index, T element) {
		add(index, wrapInList(element));
	}

	@Override
	public void add(int index, T... elements) {
		add(index, Arrays.asList(elements));
	}

	@Override
	public void add(final int index, final Collection<T> elements) {
		if (getListDataSource() != null) {
			getListDataSource().add(index, elements);
			return;
		}

		runInEventDispatch(new RunnableWithResult<Void>() {
			@Override
			public Void runWithResult() {
				getData().addAll(index, elements);
				fireEvent(new ObjectsAddedEvent<T>(index, elements));
				return null;
			}
		});
	}

	@Override
	public boolean remove(T object) {
		return remove(wrapInList(object));
	}

	@Override
	public boolean remove(T... elements) {
		return remove(Arrays.asList(elements));
	}

	@Override
	public boolean remove(final Collection<T> elements) {
		if (getListDataSource() != null) {
			return getListDataSource().remove(elements);
		}

		return runInEventDispatch(new RunnableWithResult<Boolean>() {
			@Override
			public Boolean runWithResult() {
				boolean removed = getData().removeAll(elements);
				if (removed) {
					fireEvent(new ObjectsRemovedEvent<T>(elements));
				}
				return removed;
			}
		});
	}

	@Override
	public void fireObjectsUpdatedEvent() {
		fireEvent(new ObjectsUpdatedEvent<T>(getData()));
	}

	@Override
	public void fireObjectsUpdatedEvent(T element) {
		fireObjectsUpdatedEvent(wrapInList(element));
	}

	@Override
	public void fireObjectsUpdatedEvent(T... elements) {
		fireObjectsUpdatedEvent(Arrays.asList(elements));
	}

	@Override
	public void fireObjectsUpdatedEvent(Collection<T> elements) {
		fireEvent(new ObjectsUpdatedEvent<T>(elements));
	}

	private List<T> wrapInList(T object) {
		List<T> list = new ArrayList<T>();
		list.add(object);
		return list;
	}

	@Override
	protected List<Class<?>> getSupportedBaseEvents() {
		List<Class<?>> supproted = super.getSupportedBaseEvents();
		supproted.add(ListDataSourceEvent.class);
		return supproted;
	}

	@Override
	public int indexOf(T object) {
		List<T> data = getData();
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).equals(object))
				return i;
		}
		return -1;
	}

	@Override
	public Class<T> getElementType() {
		return elementType;
	}

}
