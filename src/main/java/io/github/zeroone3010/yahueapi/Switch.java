package io.github.zeroone3010.yahueapi;

import java.util.List;

/**
 * Any switch with one or more buttons, such as a Philips Hue dimmer switch or a Philips Hue Tap switch.
 *
 * @deprecated Use the {@link io.github.zeroone3010.yahueapi.v2.Switch} class instead.
 */
public interface Switch extends Sensor {
  /**
   * Lists the buttons of this switch.
   *
   * @return A list of buttons on this switch.
   */
  List<Button> getButtons();

  /**
   * The latest event of this switch.
   *
   * @return A {@code SwitchEvent} describing the most recent button press.
   * Some switches, such as the Philips Hue Tap, only report the initial button press event.
   * Some other switches, such as the Philips Hue dimmer switch, report more events, including
   * the initial button press, another event if the button is being held down for long enough,
   * and a third event describing the release of the button. See {@link ButtonEvent.ButtonEventType}
   * for a description of the supported events.
   */
  SwitchEvent getLatestEvent();
}
