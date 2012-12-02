package org.mozzes.remoting.common.netty;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.mozzes.remoting.common.RemotingAction;

/**
 * Sends action on execution. It blocks itself until someone call {@link #setResponse(Object)} method.
 * 
 * @author Marko Jovicic <marko.jovicic@mozzartbet.com>
 * 
 */
public class FutureAction implements Callable<Object> {

  private static final Logger logger = LoggerFactory.getLogger(FutureAction.class);

  private Channel communicationChannel;
  private RemotingAction remotingAction;

  /**
   * Ensure that action will get its response.
   */
  private CountDownLatch lock = new CountDownLatch(1);

  private Object response;

  /**
   * Creates future action. This action is as soon as possible submitted for execution. After submitting special worker
   * thread will hold on for receiving response.
   * 
   * @param communicationChannel
   *          channel through which we send action
   * @param remotingAction
   *          action to be sent on execution.
   */
  public FutureAction(Channel communicationChannel, RemotingAction remotingAction) {
    this.communicationChannel = communicationChannel;
    this.remotingAction = remotingAction;
  }

  @Override
  public Object call() throws Exception {

    final long remotingCallStarted = System.currentTimeMillis();
    ChannelFuture future = communicationChannel.write(remotingAction);
    future.addListener(new ChannelFutureListener() {

      @Override
      public void operationComplete(ChannelFuture future) throws Exception {
        long remotingCallEnded = System.currentTimeMillis();
        long callDuration = remotingCallEnded - remotingCallStarted;

        logger.debug("remoting call for {} took {}ms", remotingAction.getActionName(), callDuration);
      }
    });

    // waits for the response.
    lock.await();

    return response;
  }

  /**
   * Set the response received from the server. After receiving this response client who waited for it is waken.
   * 
   * @param response
   *          represent response from the server.
   */
  public void setResponse(Object response) {

    this.response = response;

    // user has got his response - let him know that.
    lock.countDown();
  }

}
