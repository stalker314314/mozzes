package org.mozzes.remoting.client.netty;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.Timer;
import org.jboss.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * 
 * Tries to keep client connected all the time<br>
 * Reconnect occurs every 5 seconds
 * 
 * @author Bojan Blagojevic <bojan.blagojevic@mozzartbet.com>
 */
public class UptimeClientHandler extends SimpleChannelUpstreamHandler {
	
	private Logger logger = LoggerFactory.getLogger(UptimeClientHandler.class);
	
	private final ClientBootstrap bootstrap;
	private final Timer timer;
	private long startTime = -1;
	
	// Sleep 5 seconds before a reconnection attempt.
	static final int RECONNECT_DELAY = 5;

	/**
	 * Set up handler that tries to reconnect client in time manner specified by timer.
	 * Reconnection occurs only if client is disconnected, obviously.     
	 * @param bootstrap client bootstrap configuration.
	 * @param timer timer used to control reconnection.
	 */
	public UptimeClientHandler(ClientBootstrap bootstrap, Timer timer) {
		this.bootstrap = bootstrap;
		this.timer = timer;
	}

	InetSocketAddress getRemoteAddress() {
		return (InetSocketAddress) bootstrap.getOption("remoteAddress");
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		println("Sleeping for: " + RECONNECT_DELAY + "s");
		timer.newTimeout(new TimerTask() {
			@Override
			public void run(Timeout timeout) throws Exception {
				println("Reconnecting to: " + getRemoteAddress());
				bootstrap.connect();
			}
		}, RECONNECT_DELAY, TimeUnit.SECONDS);
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		if (startTime < 0) {
			startTime = System.currentTimeMillis();
		}
		println("Connected to: " + getRemoteAddress());
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		println("Disconnected from: " + getRemoteAddress());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		Throwable cause = e.getCause();
		if (cause instanceof ConnectException) {
			startTime = -1;
			println("Failed to connect: " + cause.getMessage());
		}
		ctx.getChannel().close();
	}

	void println(String msg) {
		if (startTime < 0) {
			logger.error("[SERVER IS DOWN] {}", msg);
		} else {
			logger.debug("[UPTIME: {}] \n{}", (System.currentTimeMillis() - startTime) / 1000, msg);
		}
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		println(e.getMessage().toString());
		super.messageReceived(ctx, e);
	}

	
}
