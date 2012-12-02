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
package org.mozzes.remoting.server;

import org.mozzes.remoting.common.RemotingAction;
import org.mozzes.remoting.common.RemotingActionExecutor;
import org.mozzes.remoting.common.RemotingActionExecutorProvider;
import org.mozzes.remoting.common.RemotingException;

/**
 * Zadatak dispecera akcija je da pronadje izvrsioca ( {@link RemotingActionExecutor}-a) za svaku prosledjenu akciju (
 * {@link RemotingAction})
 * 
 * @author Kokan
 * @author Perica Milosevic
 * @version 1.8.2
 */
public class RemotingActionDispatcher {

  /** maper iz imena remote akcije u fabriku njenog executor-a */
  private RemotingActionMapping actionMapping;

  RemotingActionDispatcher() {
    actionMapping = new RemotingActionMapping();
  }

  /**
   * Pronalazi i vraca izvrsioca date remote akcije
   * 
   * @param rca
   *          RemoteingAction ciji se izvrsilac trazi
   * 
   * @return RemoteingActionExecutor - izvrsilac akcije
   * 
   * @throws RemotingException
   *           Ukoliko se implementacija executor-a ne moze instancirati, ukoliko je zabranjen pristup konstruktoru
   *           implementacione klase ili ukoliko ime date klase nije mapirano u ime implementacione klase
   */
  public final RemotingActionExecutor getActionExecutor(RemotingAction rca) throws RemotingException {
    RemotingActionExecutorProvider executorProvider = actionMapping.getExecutorProvider(rca.getActionName());

    if (executorProvider != null) {
      RemotingActionExecutor executor = executorProvider.get();
      if (executor != null)
        return executor;
    }

    throw new RemotingException("No remoting action executor for action: " + rca.getActionName());
  }

  /**
   * Dodaje novo mapiranje
   */
  void addMapping(RemotingActionMapping actionMapping) {
    this.actionMapping.addMapping(actionMapping);
  }

}
