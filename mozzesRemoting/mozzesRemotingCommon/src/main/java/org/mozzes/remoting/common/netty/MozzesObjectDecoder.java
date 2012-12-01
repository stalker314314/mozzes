package org.mozzes.remoting.common.netty;

import java.io.ObjectInputStream;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;

/**
 * @author bojanb
 * Pravi Java objekat od niza bajtova
 */
public class MozzesObjectDecoder extends LengthFieldBasedFrameDecoder {

	

	public MozzesObjectDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment,
			int initialBytesToStrip) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
	}

	@Override
    protected Object decode(
            ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
        return new ObjectInputStream(
                new ChannelBufferInputStream(buffer)).readObject();
    }

	

}
