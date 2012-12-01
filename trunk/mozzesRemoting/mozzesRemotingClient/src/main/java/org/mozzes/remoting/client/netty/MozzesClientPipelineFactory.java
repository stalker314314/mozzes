package org.mozzes.remoting.client.netty;

import static org.jboss.netty.channel.Channels.pipeline;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.compression.ZlibDecoder;
import org.jboss.netty.handler.codec.compression.ZlibEncoder;
import org.jboss.netty.handler.codec.compression.ZlibWrapper;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.LengthFieldPrepender;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import org.jboss.netty.handler.logging.LoggingHandler;
import org.mozzes.remoting.common.netty.MessagePropertyEncoder;
import org.mozzes.remoting.common.netty.MozzesObjectDecoder;
import org.mozzes.remoting.common.netty.MozzesObjectEncoder;


/**
 * Pipelines connected in a sequence in meaningful way, so it
 * can handle client interaction to the server.
 * 
 * @author Bojan Blagojevic <bojan.blagojevic@mozzartbet.com>
 * @author Vladimir Todorovic
 */
public class MozzesClientPipelineFactory implements ChannelPipelineFactory {

	private final ChannelHandler businessHandler;
	private final boolean newServer;

	/**
	 * Creates client pipeline factory.
	 * 
	 * @param businessHandler - this is our business handler
	 * @param newServer - if it's <code>false</code> it is going to be created the sequence of pipelines which can handle
	 * old mozzes remoting protocol.  Otherwise, the sequence of pipelines is going to be created so clients communicate
	 * through new netty remoting protocol.
	 */
	public MozzesClientPipelineFactory(ChannelHandler businessHandler, boolean newServer) {
		this.businessHandler = businessHandler;
		this.newServer = newServer;
	}

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = pipeline();

		if(newServer) {
			createNewPipeline(pipeline);
		}else {
			createOldPipeline(pipeline);
		}
		
		// and then business logic.
		pipeline.addLast("handler", businessHandler);
		return pipeline;
	}

	/**
	 * Create new style remoting protocol where netty (Java NIO) is used as remoting protocol. Other pipelines 
	 * here ensures that requests and responses are handled well.
	 * @param pipeline pipeline onto which others are attached
	 */
	private void createNewPipeline(ChannelPipeline pipeline) {
		pipeline.addLast("deflater", new ZlibEncoder(ZlibWrapper.GZIP));
		pipeline.addLast("inflater", new ZlibDecoder(ZlibWrapper.GZIP));
		pipeline.addLast("logger", new LoggingHandler());	//DOWNSTREAM/UPSTREAM logging
		pipeline.addLast("encoder", new ObjectEncoder(1024));//DOWNSTREAM		
		pipeline.addLast("decoder", new ObjectDecoder(5242880, ClassResolvers.weakCachingResolver(null)));//UPSTREAM
	}

	/**
	 * Creates old style remoting protocol where mozzes itself on low level handled
	 * requests and responses using Java IO technique.
	 * @param pipeline pipeline onto which others are attached
	 */
	private void createOldPipeline(ChannelPipeline pipeline) {
		pipeline.addLast("logger", new LoggingHandler());  											//DOWNSTREAM/UPSTREAM logging
		pipeline.addLast("messagePropsEncode", new MessagePropertyEncoder());						//DOWNSTREAM da li se koristi kompresija i kriptovanje
		pipeline.addLast("messageSize", new LengthFieldPrepender(4));								//DOWNSTREAM duzina poruke
		pipeline.addLast("messageSizeStrip", new LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4));//UPSTREAM odseca iz poruke informaciju o duzini poruke
		pipeline.addLast("decoder", new MozzesObjectDecoder(1048576, 0, 4, 0, 0));					//UPSTREAM   deserijalizuje objekat
		pipeline.addLast("encoder", new MozzesObjectEncoder());										//DOWNSTREAM serijalizuje objekat
	}

}
