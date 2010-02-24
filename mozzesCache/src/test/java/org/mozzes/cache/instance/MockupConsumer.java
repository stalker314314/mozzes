package org.mozzes.cache.instance;

import org.mozzes.cache.instance.InstanceProvider;

class MockupConsumer extends Thread {

	private InstanceProvider<Object> ip;
	private int count;
	private volatile int instanceCount;

	MockupConsumer(InstanceProvider<Object> ip, int count) {
		super();
		this.ip = ip;
		this.count = count;
		this.instanceCount = 0;
	}

	@Override
	public void run() {
		for (int i = 0; i < count; i++) {
			try {
				ip.get();
				increaseInstanceCount();
			} catch (Exception ignore) {
			}
		}
	}

	synchronized int getInstanceCount() {
		return instanceCount;
	}

	private synchronized void increaseInstanceCount() {
		instanceCount++;
	}
}