package org.mozzes.event;

/**
 * Handler mockup event-a
 * 
 * @author Kokan
 */
public class MockupEventHandler implements MockupEvent {

	public int currentValue = 0;
	
	public MockupEventHandler(){
	}
	
	public void onEvent(int i) {
		currentValue = i;
	}
	
	public int getCurrentValue(){
		return currentValue;
	}

	@Override
	public String eventWithReturnType() {
		return new Integer(currentValue).toString();
	}
}