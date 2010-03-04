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
