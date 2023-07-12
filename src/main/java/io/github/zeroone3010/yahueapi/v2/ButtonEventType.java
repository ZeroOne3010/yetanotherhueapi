package io.github.zeroone3010.yahueapi.v2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * Button events. Note that not all switches support all kinds of events. For example, the
 * Hue Tap Switch only ever emits {@code INITIAL_PRESS} type of events, whereas the buttons of a
 * Hue Dimmer Switch emit many kind of events.
 */
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
   * The button has been pressed for a longer time. A Philips Hue dimmer switch may report this event.
   */
  LONG_PRESS("long_press"),

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

  private static final Logger logger = LoggerFactory.getLogger(ButtonEventType.class);

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
        .orElseGet(() -> {
          logger.info("Unknown button event type '{}'", eventType);
          return UNKNOWN;
        });
  }
}
