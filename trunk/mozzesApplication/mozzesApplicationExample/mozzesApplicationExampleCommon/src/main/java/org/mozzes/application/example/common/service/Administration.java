package org.mozzes.application.example.common.service;

import java.util.List;

import org.mozzes.application.example.common.exception.ExampleException;


public interface Administration<T> {
	
	public T save(T t);
	
	public void delete(T team) throws ExampleException; 
	
	public List<T> findAll();
	
	public T findById(Integer id);
	
	public List<T> findByExample(T example);

}
