package org.mozzes.application.hibernate.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;

/**
 * Base class for all DAO classes implemented using Hibernate. 
 *
 * @param <T> Class persisted by Hibernate
 * @param <ID> Type of ID class for T
 */
public abstract class GenericHibernateDAO<T, ID extends Serializable> extends HibernateDAOBase implements GenericDAO<T, ID> {

	private final Class<T> persistentClass;

	@SuppressWarnings("unchecked")
	public GenericHibernateDAO() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	public Class<T> getPersistentClass() {
		return persistentClass;
	}

	@SuppressWarnings("unchecked")
	public T findById(ID id, boolean lock) {
		T entity;
		if (lock)
			entity = (T) getSession().load(getPersistentClass(), id, LockMode.UPGRADE);
		else
			entity = (T) getSession().load(getPersistentClass(), id);

		return entity;
	}

	public List<T> findAll() {
		return findByCriteria();
	}

	@SuppressWarnings("unchecked")
	public List<T> findByExample(T exampleInstance, String... excludeProperty) {
		Example example = createExample(exampleInstance, excludeProperty);
		return getSession().createCriteria(getPersistentClass()).add(example).list();
	}

	public T save(T entity) {
		getSession().saveOrUpdate(entity);
		return entity;
	}

	public void delete(T entity) {
		getSession().delete(entity);
	}

	protected void flush() {
		getSession().flush();
	}

	protected void clear() {
		getSession().clear();
	}

	/**
	 * Use this inside subclasses as a convenience method.
	 */
	@SuppressWarnings("unchecked")
	protected List<T> findByCriteria(Criterion... criterion) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		for (Criterion c : criterion)
			crit.add(c);

		return crit.list();
	}

	private Example createExample(T exampleInstance, String... excludeProperty) {
		Example example = Example.create(exampleInstance);
		for (String exclude : excludeProperty) {
			example.excludeProperty(exclude);
		}
		return example;
	}


}