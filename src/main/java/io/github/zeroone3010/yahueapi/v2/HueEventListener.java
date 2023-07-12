package io.github.zeroone3010.yahueapi.v2;

import io.github.zeroone3010.yahueapi.v2.domain.HueEvent;
import io.github.zeroone3010.yahueapi.v2.domain.event.ButtonEvent;
import io.github.zeroone3010.yahueapi.v2.domain.event.MotionEvent;

import java.util.List;

/**
 * Implement this interface (fully or partially, as it suits your needs), then give your instance
 * as a parameter to the {@link Hue#subscribeToEvents(HueEventListener)} method to start receiving events
 * from your Bridge.
 */
public interface HueEventListener {
  /**
   * This method will be called when button events are received.
   * These events are a more specific subset of what the {@link #receive(List)} method will receive.
   * Should you choose to implement both, ensure that you do not handle button events in the other method
   * as well, or you will end up handling them as duplicates.
   *
   * @param event A button event as received from the Bridge.
   */
  default void receiveButtonEvent(ButtonEvent event) {
  }

  /**
   * This method will be called when motion events are received.
   * These events are a more specific subset of what the {@link #receive(List)} method will receive.
   * Should you choose to implement both, ensure that you do not handle motion events in the other method
   * as well, or you will end up handling them as duplicates.
   *
   * @param event A motion event as received from the Bridge.
   */
  default void receiveMotionEvent(MotionEvent event) {
  }

  /**
   * <p>This method will be called when events are received.
   * Note that this method is called for all events.
   * If you only need to know about some more specific kind of events,
   * implement the {@link #receiveButtonEvent(ButtonEvent)} method instead.
   * Note that this here method will still receive those button events as well,
   * so if you choose to implement both, ignore the button events here.</p>
   *
   * <p>Future versions of this library will have similar, more specific methods for
   * other types of events as well,
   * <a href="https://github.com/ZeroOne3010/yetanotherhueapi">please give feedback on your needs</a>.
   * </p>
   *
   * @param events A List of events as received from the Bridge.
   */
  default void receive(List<HueEvent> events) {
  }

  /**
   * This method will be called in case the event stream is closed.
   */
  default void connectionClosed() {
  }

  /**
   * This method will be called when the event stream is opened.
   */
  default void connectionOpened() {
  }
}
