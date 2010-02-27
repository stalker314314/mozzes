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