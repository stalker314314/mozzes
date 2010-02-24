package org.mozzes.application.hibernate.dao;

import org.hibernate.Session;
import org.mozzes.application.hibernate.transaction.HibernateTransaction;

import com.google.inject.Inject;
import com.google.inject.Provider;

public abstract class HibernateDAOBase {
	
	@Inject
	private Provider<HibernateTransaction> hibernateContext;

	protected Session getSession() {
		HibernateTransaction hibernateTransaction = hibernateContext.get();
		
		if (hibernateTransaction == null)
			throw new IllegalStateException("Session has not been set on DAO before usage");
		
		return hibernateTransaction.getSession();
	}

}
