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
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.mozzes.application.hibernate.HibernateConfigurationType;
import org.mozzes.application.module.scope.ApplicationScoped;

import com.google.inject.Inject;

@ApplicationScoped
class HibernateUtil {

	private SessionFactory factory;
	private Configuration configuration;

	@Inject
	private HibernateConfigurationType configurationType;
	
	Session openSession() {
		return getSessionFactory().openSession();
	}

	void closeSession(Session session) {
		session.close();
	}

	void recreateDatabase() {
		new SchemaExport(getConfiguration()).create(true, true);
	}

	private SessionFactory getSessionFactory() {
		if (factory == null)
			createFactory();
		return factory;
	}

	private Configuration getConfiguration() {
		if (configuration == null)
			createConfiguration();

		return configuration;
	}

	private synchronized void createFactory() {
		if (factory == null)
			factory = getConfiguration().buildSessionFactory();
	}

	private synchronized void createConfiguration() {
		if (configuration == null) {
			if (configurationType.equals(HibernateConfigurationType.XML))
				configuration = new Configuration().configure();
			else
				configuration = new AnnotationConfiguration().configure();
				
		}
	}
}
