package org.mozzes.event;

import org.mozzes.event.Event;
import org.mozzes.event.service.DefaultServiceRegistry;

public class EventMockup extends Event {

  public EventMockup() {
    super(new DefaultServiceRegistry());
  }

}
