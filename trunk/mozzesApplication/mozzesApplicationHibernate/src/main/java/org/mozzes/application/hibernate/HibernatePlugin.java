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
