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
