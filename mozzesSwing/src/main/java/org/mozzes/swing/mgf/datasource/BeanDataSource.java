package org.mozzes.swing.mgf.datasource;

import java.util.Set;

import javax.validation.ConstraintViolation;

/**
 * Provides one bean
 * 
 * @author milos
 * 
 * @param <T> Type of the bean provided by the source
 */
public interface BeanDataSource<T> extends DataSource<T> {
	Set<ConstraintViolation<T>> validate(Class<?>... groups);
}
