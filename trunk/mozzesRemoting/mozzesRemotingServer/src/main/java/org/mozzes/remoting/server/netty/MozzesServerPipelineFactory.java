package org.mozzes.remoting.server.netty;

import static org.jboss.netty.channel.Channels.pipeline;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.compression.ZlibDecoder;
import org.jboss.netty.handler.codec.compression.ZlibEncoder;
import org.jboss.netty.handler.codec.compression.ZlibWrapper;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.logging.LoggingHandler;
import org.mozzes.remoting.server.RemotingActionDispatcher;

/**
 * Server pipeline factory used to create server business logic for processing clients requests.
 * 
 * @author Bojan Blagojevic <bojan.blagojevic@mozzartbet.com>
 * @author Vladimir Todorovic
 */
public class MozzesServerPipelineFactory implements ChannelPipelineFactory {

  private final RemotingActionDispatcher dispatcher;
  private final ExecutionHandler executionHandler;

  /**
   * Creates server pipeline factory.
   * 
   * @param dispatcher
   *          - dispatches remote actions to the action executors
   * @param executionHandler
   *          - executes channel events without blocking I/O worker threads
   */
  public MozzesServerPipelineFactory(RemotingActionDispatcher dispatcher, ExecutionHandler executionHandler) {
    this.dispatcher = dispatcher;
    this.executionHandler = executionHandler;
  }

  @Override
  public ChannelPipeline getPipeline() throws Exception {
    ChannelPipeline pipeline = pipeline();
    createNewPipeline(pipeline);

    return pipeline;
  }

  /**
   * On the given pipeline attach our pipes which creates Netty server logic. This logic process Netty clients requests.
   * 
   * @param pipeline
   *          - pipeline to attach other logic
   */
  private void createNewPipeline(ChannelPipeline pipeline) {
    pipeline.addLast("deflater", new ZlibEncoder(ZlibWrapper.GZIP));
    pipeline.addLast("inflater", new ZlibDecoder(ZlibWrapper.GZIP));
    pipeline.addLast("logger", new LoggingHandler());
    pipeline.addLast("encoder", new ObjectEncoder(1024));
    pipeline.addLast("decoder", new ObjectDecoder(5242880, ClassResolvers.weakCachingConcurrentResolver(null)));
    pipeline.addLast("identificationHandler", new IdentificationHandler());
    pipeline.addLast("executor", executionHandler);
    pipeline.addLast("handler", new MozzesServerHandler(dispatcher));
  }
}
