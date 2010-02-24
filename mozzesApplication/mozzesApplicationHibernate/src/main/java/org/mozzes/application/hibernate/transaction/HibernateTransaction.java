package org.mozzes.application.hibernate.transaction;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.mozzes.application.module.scope.TransactionScoped;
import org.mozzes.application.plugin.transaction.TransactionException;

import com.google.inject.Inject;

@TransactionScoped
public class HibernateTransaction {

	private Session session;
	private Transaction transaction;
	
	@Inject
	private HibernateUtil hibernateUtil;

	public Session getSession() {
		return session;
	}
	
	void begin() {
		if (session != null)
			throw new TransactionException("Illegal transaction state, transaction already exists");
		
		this.session = hibernateUtil.openSession();
		this.transaction = session.beginTransaction();
	}
	
	void commit() {
		if (transaction == null)
			throw new TransactionException("Illegal transaction state, transaction doesn't exist");
		
		transaction.commit();
	}
	
	void rollback() {
		if (transaction != null)
			transaction.rollback();
	}
	
	void clear() {
		transaction = null;
		if (session != null) {
			try {
				hibernateUtil.closeSession(session);
			} finally {
				session = null;
			}
		}
	}

}
