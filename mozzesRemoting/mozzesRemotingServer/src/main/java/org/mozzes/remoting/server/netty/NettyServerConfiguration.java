package org.mozzes.remoting.server.netty;

/**
 * Netty server configuration used for netty server.
 * @author Marko Jovicic <marko.jovicic@mozzartbet.com>
 *
 */
public class NettyServerConfiguration {
	
	private Integer ioworkers;
	private Integer workers;
	
	private Integer memoryPerChannel;
	private Integer memoryPerPool;
	
	private Integer nettyPort;

	/**
	 * Netty server configuration with default settings. 
	 * Please note that server port is <code>null</code>
	 * meaning netty is disabled. Enable server by 
	 * calling {@link #setNettyPort(Integer)} method.
	 */
	public NettyServerConfiguration() {
		ioworkers = 8;
		workers = 40;
		memoryPerPool = 52428800;
		memoryPerChannel = 1048576;
		
		nettyPort = null;
	}
	
	public Integer getIoworkers() {
		return ioworkers;
	}

	public void setIoworkers(Integer ioworkers) {
		if (ioworkers == null) {
			return;
		}
		this.ioworkers = ioworkers;
	}

	public Integer getWorkers() {
		return workers;
	}

	public void setWorkers(Integer workers) {
		if (workers == null) {
			return;
		}
		
		this.workers = workers;
	}

	public Integer getMemoryPerChannel() {
		return memoryPerChannel;
	}

	public void setMemoryPerChannel(Integer memoryPerChannel) {
		if (memoryPerChannel == null) {
			return;
		}
		
		this.memoryPerChannel = memoryPerChannel;
	}

	public Integer getMemoryPerPool() {
		return memoryPerPool;
	}

	public void setMemoryPerPool(Integer memoryPerPool) {
		if (memoryPerPool == null) {
			return;
		}
		
		this.memoryPerPool = memoryPerPool;
	}

	public Integer getNettyPort() {
		return nettyPort;
	}

	public void setNettyPort(Integer nettyPort) {
		this.nettyPort = nettyPort;
	}		
	
	/**
	 * Netty is enabled if netty port is specified.
	 * @return <code>true</code> if netty is enabled, <code>false</code> otherwise.
	 */
	public boolean isNettyEnabled() {
		return nettyPort != null;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NettyServerConfiguration is ");
		builder.append(isNettyEnabled() ? "ENABLED" : "DISABLED");
		builder.append("\nParameters are:");
		builder.append("\nioworkers=");
		builder.append(ioworkers);
		builder.append("\nworkers=");
		builder.append(workers);
		builder.append("\nmemoryPerChannel=");
		builder.append(memoryPerChannel);
		builder.append("\nmemoryPerPool=");
		builder.append(memoryPerPool);
		builder.append("\nnettyPort=");
		builder.append(nettyPort);

		return builder.toString();
	}
	
	
	
}
