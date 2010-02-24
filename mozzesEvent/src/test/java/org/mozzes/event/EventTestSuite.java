package org.mozzes.event;

import junit.framework.Test;
import junit.framework.TestSuite;

public class EventTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("Event test suite (com.edg.mozzes)");
		// $JUnit-BEGIN$
		suite.addTestSuite(TestLocalEventWithoutQueue.class);
		suite.addTestSuite(TestLocalEventWithQueue.class);
		suite.addTestSuite(TestConfiguration.class);
		// $JUnit-END$
		return suite;
	}
}