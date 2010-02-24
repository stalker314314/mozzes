package org.mozzes.remoting.client.pool;

import org.mozzes.remoting.client.RemotingClient;
import org.mozzes.remoting.client.RemotingExecutorProviderFactory;
import org.mozzes.remoting.common.RemotingAction;
import org.mozzes.remoting.common.RemotingActionExecutor;
import org.mozzes.remoting.common.RemotingException;
import org.mozzes.remoting.common.RemotingResponse;


/**
 * Action executor useful in conjunction with remoting clients pool. Wraps
 * {@link RemotingClientPoolWrapper#execute(RemotingAction)} action by picking client from pool, calling execution and
 * returning client back to pool. Clients shouldn't use this class, for further direction look at
 * {@link RemotingExecutorProviderFactory}
 * 
 * @author Kokan
 */
class RemotingClientPoolWrapper implements RemotingActionExecutor {

    /** Pool to use for getting remoting clients */
    private final RemotingClientPool pool;

    /**
     * Constructor with specified pool
     */
    RemotingClientPoolWrapper(RemotingClientPool pool) {
        this.pool = pool;
    }

    @Override
    public RemotingResponse execute(RemotingAction action) throws RemotingException {
        RemotingClient remotingClient = null;
        try {
            remotingClient = pool.getClient();
            return remotingClient.execute(action);
        } finally {
            if (remotingClient != null) {
                pool.closeClient(remotingClient);
            }
        }
    }
}
