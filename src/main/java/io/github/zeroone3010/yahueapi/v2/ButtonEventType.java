package io.github.zeroone3010.yahueapi.v2;

import java.util.Objects;
import java.util.stream.Stream;

public enum ButtonEventType {
  /**
   * An event was reported but its nature is unclear, not supported by this library.
   */
  UNKNOWN(null),

  /**
   * The button was pushed. This is the only kind of event reported by e.g. a Philips Hue Tap switch.
   */
  INITIAL_PRESS("initial_press"),

  /**
   * The button is being held down. A Philips Hue dimmer switch will report this event.
   */
  HOLD("repeat"),

  /**
   * The button was released after a brief push. A Philips Hue dimmer switch will report this event.
   */
  SHORT_RELEASED("short_release"),

  /**
   * The button was released after it was held down for a while.  A Philips Hue dimmer switch will report this event.
   */
  LONG_RELEASED("long_release"),

  /**
   * "Double short release".
   */
  DOUBLE_SHORT_RELEASED("double_short_release");

  private final String eventType;

  ButtonEventType(final String eventType) {
    this.eventType = eventType;
  }

  private String getEventType() {
    return eventType;
  }

  public static ButtonEventType parseFromButtonEventType(final String eventType) {
    return Stream.of(values())
        .filter(value -> Objects.equals(value.getEventType(), eventType))
        .findFirst()
        .orElse(UNKNOWN);
  }
}
