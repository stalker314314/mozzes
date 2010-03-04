/*
 * Copyright 2010 Mozzart
 *
 *
 * This file is part of mozzes.
 *
 * mozzes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mozzes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mozzes.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
