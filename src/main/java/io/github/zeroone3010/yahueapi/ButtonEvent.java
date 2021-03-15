package io.github.zeroone3010.yahueapi;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * <p>
 * This class describes an event of a single button of a switch. Depending on the device in question,
 * a button may only report when it is pushed down. Other kinds of devices will report the release of the button
 * separately, and still some other devices may report whether the button was held down for a short period
 * of time or a long period of time before it was released.
 * </p>
 *
 * <p>
 * Every button event has a numeric event code, an integer.
 * In case the device does not introduce all of its events to the Bridge, this library will not be able to know
 * about those events and will report them as the {@code UNKNOWN} type.
 * However, even in those cases one will be able to tell different {@code UNKNOWN} events apart by examining the
 * {@code eventCode}.
 * </p>
 */
public final class ButtonEvent {
  /**
   * An enumeration of the actions that a user may take with a physical button.
   */
  public enum ButtonEventType {
    /**
     * An event was reported but its nature is unclear. It may originate from, say, a Friends of Hue compatible device.
     * See the accompanying event code to distinguish this event from the rest.
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
    LONG_RELEASED("long_release");

    private final String eventType;

    private ButtonEventType(final String eventType) {
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

  private final ButtonEventType eventType;
  private final int eventCode;

  ButtonEvent(final ButtonEventType eventType, final int eventCode) {
    this.eventType = eventType;
    this.eventCode = eventCode;
  }

  public ButtonEventType getEventType() {
    return eventType;
  }

  public int getEventCode() {
    return eventCode;
  }

  @Override
  public String toString() {
    return "ButtonEvent{" +
        "eventType=" + eventType +
        ", eventCode=" + eventCode +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final ButtonEvent that = (ButtonEvent) o;
    return eventCode == that.eventCode &&
        eventType == that.eventType;
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventType, eventCode);
  }
}
