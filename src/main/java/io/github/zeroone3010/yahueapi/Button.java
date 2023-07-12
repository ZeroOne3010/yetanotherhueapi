package io.github.zeroone3010.yahueapi;

import java.util.List;

/**
 * A button of a {@link Switch}.
 *
 * @deprecated Use the {@link io.github.zeroone3010.yahueapi.v2.Button} class instead.
 */
public interface Button {

  /**
   * <p>The number of this button in a switch.</p>
   *
   * <p>For a Philips Hue dimmer switch,<br>
   * <strong>1</strong> is the ON button,<br>
   * <strong>2</strong> is the button that makes the lights brighter,<br>
   * <strong>3</strong> is the button that makes the lights dimmer, and<br>
   * <strong>4</strong> is the OFF button.
   * </p>
   *
   * <p>For a Philips Hue Tap switch,<br>
   * <strong>1</strong> is the button with one dot,<br>
   * <strong>2</strong> is the button with two dots,<br>
   * <strong>3</strong> is the button with three dots, and<br>
   * <strong>4</strong> is the button with four dots.
   * </p>
   *
   * <p>Other devices, such as the Friends of Hue ones, will have their buttons in some order that one just
   * needs to find out by pushing each one and looking at the result of the {@link Switch#getLatestEvent()}
   * method after each push.</p>
   *
   * <p>Furthermore, some devices may have "virtual" buttons that this library cannot know about.
   * These devices include at least the Senic Friends of Hue switch that has two rocker keys, i.e. four physical
   * buttons. However, when one pushes both rockers up (or down) simultaneously, the device logs events
   * from virtual buttons "5" and "6", which are never actually introduced to the Hue Bridge.
   * This library will handle those presses as "button number -1", but one will still be able to tell them
   * apart by inspecting the event code.</p>
   *
   * @return An integer describing the number of this button. If the device does not introduce all of its buttons
   * to the Hue Bridge, such buttons will have the number "-1".
   */
  int getNumber();

  /**
   * A list of events that may be performed with the button. The buttons of some devices may produce
   * several kinds of events, while other types might produce only the {@link ButtonEvent.ButtonEventType INITIAL_PRESS} event.
   * This list may not be comprehensive, so a button may end up returning events of {@link ButtonEvent.ButtonEventType UNKNOWN}
   * type even if they are not listed here.
   *
   * @return A List of {@link ButtonEvent} object, describing the types of events that this button may perform.
   * Note that this list may not be comprehensive, as at least some Friends of Hue brand of switches are known
   * to produce events that they do not initially introduce to the Bridge.
   */
  List<ButtonEvent> getPossibleEvents();
}
