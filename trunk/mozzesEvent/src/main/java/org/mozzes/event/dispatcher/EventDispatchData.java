package org.mozzes.event.dispatcher;

/**
 * Podaci potrebni za dispatch jednog dogadjaja.
 *
 * @author Perica Milosevic
 * @version 1.7.7
 */
public class EventDispatchData {

   /**
    * Podaci o dogadjaju
    */
   private EventDetails eventDetails;

   /**
    * Handler koji ce obraditi dogadjaj
    */
   private Object eventHandler;
   
   /**
    * Vreme kada je event nastao
    */
   private long eventCreationTime;

   EventDispatchData(EventDetails eventDetails, Object eventHandler) {
      this.eventDetails = eventDetails;
      this.eventHandler = eventHandler;
      this.eventCreationTime = System.currentTimeMillis();
   }

   /**
    * Obrada dogadjaja
    * @throws Exception Ukoliko je doslo do greske prilikom obrade
    */
   public void dispatch() throws Throwable {
      EventPerformanceLogger.get().addEvent(eventCreationTime, eventDetails.getMethodSignature());
      eventDetails.dispatch(eventHandler);
   }

   @Override
public String toString() {
      return "Event = " + eventDetails + ", Handler = " + eventHandler;
   }
}
