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
class RemotingActionDispatcher {

    /** maper iz imena remote akcije u fabriku njenog executor-a */
    private RemotingActionMapping actionMapping;

    RemotingActionDispatcher() {
        actionMapping = new RemotingActionMapping();
    }

    /**
     * Pronalazi i vraca izvrsioca date remote akcije
     * 
     * @param rca RemoteingAction ciji se izvrsilac trazi
     * 
     * @return RemoteingActionExecutor - izvrsilac akcije
     * 
     * @throws RemotingException Ukoliko se implementacija executor-a ne moze instancirati, ukoliko je zabranjen pristup
     *             konstruktoru implementacione klase ili ukoliko ime date klase nije mapirano u ime implementacione
     *             klase
     */
    final RemotingActionExecutor getActionExecutor(RemotingAction rca) throws RemotingException {
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
    void addMapping(RemotingActionMapping actionMapping){
        this.actionMapping.addMapping(actionMapping);
    }

}
