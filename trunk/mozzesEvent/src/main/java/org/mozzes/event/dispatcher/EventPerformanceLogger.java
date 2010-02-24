package org.mozzes.event.dispatcher;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Klasa koja prati broj izvrsenih evenata i vreme koje oni provedu na cekanju dok se ne izvrsi obrada.
 * 
 * Klasa prati smo handlere koji su prijavljeni da prate evente preko queue-a
 * 
 * @author Perica Milosevic
 * @version 1.8.2
 */
class EventPerformanceLogger {
	private static final Logger logger = LoggerFactory.getLogger(EventPerformanceLogger.class);

	// jedinstvena instanca - singleton
	private static final EventPerformanceLogger UNIQUE_INSTANCE = new EventPerformanceLogger();

	// da li se obavlja logovanje?
	private static final boolean ENABLED = true;

	// da li je potrebno obavljati merenje na nivou svake pojedinacne metode?
	// (ako je iskljucenog logovanje onda se ovaj parametar nece uzimati u obzir tj. nece biti merenja na nivou metode)
	private static final boolean TRACE_METHODS = true;

	// period u kome se obavlja logovanje izvresnog merenja
	private static final long PERIOD = 300000; // 5 minuta

	/**
	 * Singleton getInstance
	 * 
	 * @return jedinstvena instanca
	 */
	static EventPerformanceLogger get() {
		return UNIQUE_INSTANCE;
	}

	/**
	 * Performanse obrade dogadjaja od prethodnog merenja
	 */
	private final EventPerformance periodPerformance = new EventPerformance();

	/**
	 * Performanse obrade dogadjaja od pokretanja servera
	 */
	private final EventPerformance totalPerformance = new EventPerformance();

	/**
	 * Performanse obrade dogadjaja za svaki event metod pojedinacno
	 * 
	 */
	private final HashMap<String, EventPerformance> performanceByMethod = new HashMap<String, EventPerformance>();

	private EventPerformanceLogger() {
		if (ENABLED)
			new EventPerformanceLoggerThread().start();
	}

	/**
	 * Event je dosao na red za obradu
	 * 
	 * @param eventCreationTime vreme nastanka eventa
	 * @param methodSignature - potpis metode, jedinstvene identifikuje metodu
	 */
	public void addEvent(long eventCreationTime, String methodSignature) {
		if (ENABLED)
			increaseMetrics(System.currentTimeMillis() - eventCreationTime, methodSignature);
	}

	/**
	 * Uvecava broj evenata od prethodnog merenja i vreme cekanja
	 * 
	 * @param waitTime vreme koje je novi event cekao na obradu
	 * @param methodSignature - potpis metode, jedinstvene identifikuje metodu
	 */
	private void increaseMetrics(long waitTime, String methodSignature) {
		periodPerformance.add(waitTime);

		if (TRACE_METHODS) {
			EventPerformance methodPerformance = performanceByMethod.get(methodSignature);
			if (methodPerformance == null) {
				synchronized (performanceByMethod) {
					methodPerformance = performanceByMethod.get(methodSignature);
					if (methodPerformance == null) {
						methodPerformance = new EventPerformance();
						performanceByMethod.put(methodSignature, methodPerformance);
					}
				}
			}
			methodPerformance.add(waitTime);
		}
	}

	/**
	 * Zapisuje u logu sve metrike od prethodnog merenja i resetuje metrike za novo merenje.
	 */
	private void log() {
		final EventPerformance tmpPerformance = periodPerformance.reset();
		totalPerformance.add(tmpPerformance);

		TreeMap<String, EventPerformance> tmpMethodPerformance = new TreeMap<String, EventPerformance>();
		synchronized (performanceByMethod) {
			tmpMethodPerformance.putAll(performanceByMethod);
			performanceByMethod.clear();
		}

		StringBuffer logString = new StringBuffer();
		logString.append("Event dispatch metrics since server startup: ");
		logString.append("\n\tevent count      : ").append(totalPerformance.eventCount);
		logString.append("\n\tsummary wait time: ").append(totalPerformance.eventWaitTime).append("ms");
		logString.append("\n\taverage wait time: ").append(roundDecimal(totalPerformance.averageWaitTime(), 2)).append(
				"ms\n\n");
		logString.append("Event dispatch metrics during last ").append(PERIOD / 1000).append("s:");
		logString.append("\n\tevent count      : ").append(tmpPerformance.eventCount);
		logString.append("\n\tsummary wait time: ").append(tmpPerformance.eventWaitTime).append("ms");
		logString.append("\n\taverage wait time: ").append(roundDecimal(tmpPerformance.averageWaitTime(), 2)).append(
				"ms");

		if (TRACE_METHODS && (tmpPerformance.eventCount > 0)) {
			logString.append("\n\nEvent dispatch metrics by method during last ").append(PERIOD / 1000).append("s:");
			for (Iterator<String> methodIterator = tmpMethodPerformance.keySet().iterator(); methodIterator.hasNext();) {
				String methodSignature = methodIterator.next();
				EventPerformance ep = tmpMethodPerformance.get(methodSignature);
				logString.append("\n\t").append(methodSignature).append(": ");
				logString.append("event count = ").append(ep.eventCount);
				logString.append(", summary wait time = ").append(ep.eventWaitTime).append("ms");
				logString.append(", average wait time = ").append(roundDecimal(ep.averageWaitTime(), 2)).append("ms");
			}
		}
		logger.info(logString.toString());
	}

	private class EventPerformanceLoggerThread extends Thread {

		private EventPerformanceLoggerThread() {
			super("EventPerformanceLogger");
			setDaemon(true);
			setPriority(MIN_PRIORITY);
		}

		@Override
		public void run() {
			try {
				while (true) {
					sleep(PERIOD);
					log();
				}
			} catch (InterruptedException e) {
				logger.warn("EventPerformanceLogger interrupted");
			}
		}
	}

	private class EventPerformance {
		private long eventCount;
		private long eventWaitTime;

		private EventPerformance(long eventCount, long eventWaitTime) {
			this.eventCount = eventCount;
			this.eventWaitTime = eventWaitTime;
		}

		private EventPerformance() {
			this(0l, 0l);
		}

		private double averageWaitTime() {
			return eventCount != 0 ? eventWaitTime / 1d / eventCount : 0d;
		}

		private synchronized void add(long waitTime) {
			eventCount++;
			eventWaitTime += waitTime;
		}

		private synchronized void add(EventPerformance other) {
			eventCount += other.eventCount;
			eventWaitTime += other.eventWaitTime;
		}

		private synchronized EventPerformance reset() {
			EventPerformance returnValue = new EventPerformance(eventCount, eventWaitTime);
			this.eventCount = 0l;
			this.eventWaitTime = 0l;
			return returnValue;
		}
	}

	/**
	 * Zaokruzivanje na odredjeni broj decimala
	 * 
	 * @param number double broj koji zaokruzujemo
	 * @param decimalPlaces int zeljeni broj decimala (minimum je 1)
	 * @return double vrednost zaokruzena na zeljeni broj decimala
	 */
	public static double roundDecimal(double number, int decimalPlaces) {
		if (decimalPlaces < 1)
			throw new IllegalArgumentException();

		double power = Math.pow(10, decimalPlaces);
		return Math.round(number * power) / power;
	}
}
