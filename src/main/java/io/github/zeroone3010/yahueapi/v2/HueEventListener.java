package io.github.zeroone3010.yahueapi.v2;

import io.github.zeroone3010.yahueapi.v2.domain.HueEvent;

import java.util.List;

public interface HueEventListener {
  /**
   * This method will be called when events are received.
   * @param events A List of events as received from the Bridge
   */
  void receive(List<HueEvent> events);

  /**
   * This method will be called in case the event stream is closed.
   */
  default void connectionClosed() {}

  /**
   * This method will be called when the event stream is opened.
   */
  default void connectionOpened() {}
}
