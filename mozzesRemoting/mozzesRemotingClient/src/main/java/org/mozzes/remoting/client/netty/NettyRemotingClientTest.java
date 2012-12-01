package org.mozzes.remoting.client.netty;

import org.mozzes.remoting.common.RemotingConfiguration;

public class NettyRemotingClientTest {
	
	public static void main(String[] args) throws Exception {
		NettyRemotingClient client = new NettyRemotingClient(new RemotingConfiguration("127.0.0.1", 5321));
		client.connect();
		System.out.println(client.isConnected());
		client.disconnect();
		
	}

}
