package org.mozzes.remoting.common.netty;

import static org.jboss.netty.buffer.ChannelBuffers.dynamicBuffer;

import java.io.IOException;
import java.io.ObjectOutputStream;

import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

/**
 * @author bojanb Serijalizuje objekat
 */
public class MozzesObjectEncoder extends OneToOneEncoder {

  @Override
  protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
    ObjectOutputStream objectOutputStream = null;

    try {
      ChannelBufferOutputStream bout = new ChannelBufferOutputStream(dynamicBuffer(1024, ctx.getChannel().getConfig()
          .getBufferFactory()));

      objectOutputStream = new ObjectOutputStream(bout);
      objectOutputStream.writeObject(msg);
      objectOutputStream.flush();

      return bout.buffer();
    } finally {
      if (objectOutputStream != null) {
        try {
          objectOutputStream.close();
        } catch (IOException ioe) {
        }
      }
    }
  }

}
