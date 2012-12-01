package org.mozzes.remoting.common.netty;

import java.util.concurrent.atomic.AtomicBoolean;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;

/**
 * This one decode message and it is used when dealing with old remoting protocol.
 * Netty support the old one protocol.
 * 
 * @author Bojan Blagojevic <bojan.blagojevic@mozzartbet.com>
 *
 */
public class MessagePropertyDecoder extends OneToOneDecoder {
	private AtomicBoolean received = new AtomicBoolean(false);

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
		if (received.get()) {
			return msg;
		}
		try {
			if (!(msg instanceof ChannelBuffer)) {
				return msg;
			}
			ChannelBuffer body = (ChannelBuffer) msg;			
			body.readByte();
			body.readByte();

			return body;
		} finally {
			received.set(true);
		}

	}

}
