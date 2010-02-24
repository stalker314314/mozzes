package org.mozzes.application.hibernate.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericDAO<T, ID extends Serializable> {

	T findById(ID id, boolean lock);

	List<T> findAll();

	List<T> findByExample(T exampleInstance, String... excludeProperty);

	T save(T entity);

	void delete(T entity);

}
