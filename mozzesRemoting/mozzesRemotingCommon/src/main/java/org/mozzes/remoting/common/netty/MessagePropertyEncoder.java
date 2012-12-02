package org.mozzes.remoting.common.netty;

import static org.jboss.netty.buffer.ChannelBuffers.*;

import java.util.concurrent.atomic.AtomicBoolean;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

/**
 * MozzesRemoting protokol zahteva u PRVOJ poruci informaciju o tome da li je poruka kompresovana ili enkriptovana.<br>
 * Ovaj encoder oba propertija za sada stavlja na false :)
 * 
 * @author bojanb
 * 
 */
public class MessagePropertyEncoder extends OneToOneEncoder {

  /**
   * Da li je neka poruka vec poslata
   */
  private AtomicBoolean sent = new AtomicBoolean(false);

  @Override
  protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
    if (sent.get()) {
      return msg;
    }
    try {
      if (!(msg instanceof ChannelBuffer)) {
        return msg;
      }
      ChannelBuffer body = (ChannelBuffer) msg;
      ChannelBuffer header = channel.getConfig().getBufferFactory().getBuffer(body.order(), 2);
      header.writeShort(0);

      return wrappedBuffer(header, body);
    } finally {
      sent.set(true);
    }

  }

}
