package org.mozzes.cache.instance;

import org.mozzes.cache.instance.InstanceProvider;

public class MockupInstanceProvider implements InstanceProvider<Object> {

	private int instanceCount = 0;

	@Override
	public Object get() throws Exception {
		instanceCount++;
		return new Object();
	}

	public int getInstanceCount() {
		return instanceCount;
	}
}
