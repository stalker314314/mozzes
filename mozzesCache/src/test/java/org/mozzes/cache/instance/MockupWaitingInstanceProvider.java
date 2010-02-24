package org.mozzes.cache.instance;

import org.mozzes.cache.instance.InstanceProvider;

public class MockupWaitingInstanceProvider implements InstanceProvider<Object> {

	private volatile int instanceCount = 0;
	
	private Object lock = new Object();
	private boolean ready = false;
	
	MockupWaitingInstanceProvider() {
	}

	@Override
	public Object get() throws Exception {
		waitSignal();
		instanceCount++;
		return new Object();
	}

	public int getInstanceCount() {
		return instanceCount;
	}
	
	boolean isReady() {
		synchronized (lock) {
			return ready;
		}
	}

	
	void go() {
		synchronized (lock) {
			lock.notify();
		}
	}
	
	private void waitSignal() {
		synchronized (lock) {
			ready = true;
			try {
				lock.wait();
			} catch (InterruptedException ignore) {
			}
		}
	}
	
	
}
