package org.mozzes.cache.instance;

import org.mozzes.cache.instance.InstanceCache;

import junit.framework.TestCase;

public class TestInstanceCache extends TestCase {

	private MockupInstanceProvider instanceProvider;
	private InstanceCache<Object> cache;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		instanceProvider = new MockupInstanceProvider();
		cache = new InstanceCache<Object>(instanceProvider);
	}

	public void testInitialization() throws Exception {
		assertEquals(false, cache.isInstanceInitialized());
		cache.get();
		assertEquals(true, cache.isInstanceInitialized());
		cache.clear();
		assertEquals(false, cache.isInstanceInitialized());
		cache.get();
		assertEquals(true, cache.isInstanceInitialized());
	}

	public void testCaching() throws Exception {
		assertEquals(0, instanceProvider.getInstanceCount());
		cache.get();
		assertEquals(1, instanceProvider.getInstanceCount());
		cache.get();
		assertEquals(1, instanceProvider.getInstanceCount());
		cache.clear();
		assertEquals(1, instanceProvider.getInstanceCount());
		cache.get();
		assertEquals(2, instanceProvider.getInstanceCount());
		cache.get();
		assertEquals(2, instanceProvider.getInstanceCount());
	}

	public void testConcurrent() throws Exception {
		MockupWaitingInstanceProvider mwip = new MockupWaitingInstanceProvider();
		InstanceCache<Object> waitingCache = new InstanceCache<Object>(mwip);
		assertEquals(0, mwip.getInstanceCount());

		final int instancesPerConsumer = 100;
		MockupConsumer[] consumers = new MockupConsumer[5];
		for (int i = 0; i < consumers.length; i++) {
			consumers[i] = new MockupConsumer(waitingCache, instancesPerConsumer);
			consumers[i].start();
		}
		
		while (!mwip.isReady())
			Thread.sleep(instancesPerConsumer);
		assertEquals(0, mwip.getInstanceCount());
		
		for (int i = 0; i < consumers.length; i++) 
			assertEquals(0, consumers[i].getInstanceCount());

		mwip.go();

		for (int i = 0; i < consumers.length; i++)
			consumers[i].join();

		assertEquals(1, mwip.getInstanceCount());
		
		
		for (int i = 0; i < consumers.length; i++) 
			assertEquals(instancesPerConsumer, consumers[i].getInstanceCount());
	}

}
