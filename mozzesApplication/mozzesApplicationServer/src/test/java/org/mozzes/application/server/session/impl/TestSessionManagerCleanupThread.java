package org.mozzes.application.server.session.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;
import org.mozzes.application.server.session.impl.SessionContext;
import org.mozzes.application.server.session.impl.SessionManagerCleanupThread;

public class TestSessionManagerCleanupThread {


	@Test
	public void testThreadCleaning(){
		
		ConcurrentHashMap<String, SessionContext> map = new ConcurrentHashMap<String, SessionContext>();
		SessionManagerCleanupThread thread = new SessionManagerCleanupThread(map);
		thread.setCleanupInterval(100);
		thread.start();
		int startSize = map.size();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}
		
		assertEquals(startSize,map.size());
		
		thread.stopRunning();
	}	

	
	@Test
	public void testThreadRunning(){
		ConcurrentHashMap<String, SessionContext> map = new ConcurrentHashMap<String, SessionContext>();
		SessionManagerCleanupThread thread = new SessionManagerCleanupThread(map);
		thread.setCleanupInterval(100);
		thread.start();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}
		
		thread.stopRunning();
	}	

	@Test
	public void testCleaningSomeSessions(){
		ConcurrentHashMap<String, SessionContext> map = new ConcurrentHashMap<String, SessionContext>();

		int longSessionsCount = 0;
		
		int sessionCount = (int) (Math.random()*100);

		for(int i = 0;i<sessionCount;i++){
			
			boolean isLongSession = Math.random()<0.5;//~50% are long
			
			if(isLongSession)
				longSessionsCount++;
			
			addNewContext(map,isLongSession);
		}
		
		assertEquals(sessionCount,map.size()); //now all sessions are in the map
		
		SessionManagerCleanupThread thread = new SessionManagerCleanupThread(map);
		thread.setCleanupInterval(100);
		thread.start();
		try {
			Thread.sleep(1000);
			//after some time cleanup should be triggered and sessions that are not long are gone
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}
		
		assertEquals(longSessionsCount,map.size()); //only long sessions remained
				
		thread.stopRunning();
	}

	private void addNewContext(ConcurrentHashMap<String, SessionContext> map,boolean longSession) {
		SessionContext context = new SessionContext();
		context.setSessionId(Math.random()+"");//generate id
		context.setUserAuthorized(true);	
		if(longSession)
			context.setSessionExpirationTime(Integer.MAX_VALUE);
		else
			context.setSessionExpirationTime(20);
		
		map.put(context.getSessionId(), context);
	}	
}
