package org.mozzes.application.hibernate;

import org.mozzes.application.hibernate.dao.GenericHibernateDAO;
import org.mozzes.application.hibernate.transaction.HibernateTransactionManager;
import org.mozzes.application.plugin.transaction.TransactionManager;
import org.mozzes.application.plugin.transaction.TransactionPlugin;

import com.google.inject.Binder;

/**
 * HibernatePlugin provides Hibernate integration for Mozzes server.<br>
 * <br>
 * Extend {@link GenericHibernateDAO} with your DAO classes and inject them into your services as needed. 
 */
public class HibernatePlugin extends TransactionPlugin {
	
	private final HibernateConfigurationType configurationType;
	
	public HibernatePlugin(HibernateConfigurationType configurationType) {
		this.configurationType = configurationType;
	}

	@Override
	public Class<? extends TransactionManager> getTransactionManager() {
		return HibernateTransactionManager.class;
	}
	
	@Override
	public void doCustomBinding(Binder binder) {
		binder.bind(HibernateConfigurationType.class).toInstance(configurationType);
	}

}
