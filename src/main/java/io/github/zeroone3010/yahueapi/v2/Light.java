package io.github.zeroone3010.yahueapi.v2;

import io.github.zeroone3010.yahueapi.v2.domain.update.UpdateLight;

import java.util.UUID;

/**
 * A single Philips Hue -compatible light. Could be a bulb, a ceiling fixture, a LED strip, or anything in between.
 * Even remote controlled wall sockets are considered as "lights" by the Hue Bridge.
 *
 * @since 3.0.0
 */
public interface Light {

  /**
   * <p>Returns the technical id of the light, as assigned by the Bridge. The id stays the same even if the light name
   * is changed by the user.</p>
   *
   * @return Id of the light.
   */
  UUID getId();

  /**
   * Returns the name of the light, as set by the user.
   *
   * @return Name of the light.
   */
  String getName();

  /**
   * Turns the light on.
   */
  void turnOn();

  /**
   * Turns the light off.
   */
  void turnOff();

  /**
   * Queries the light state.
   *
   * @return True if the light is on, false if it is off.
   */
  boolean isOn();

  /**
   * Sets the brightness of the light. If the light is off, does not turn it on, nor does {@code 0} turn it off.
   *
   * @param brightness A value from {@code 1} (minimum brightness) to {@code 100} (maximum brightness).
   */
  void setBrightness(int brightness);

  /**
   * Sets a state for the light.
   *
   * @param state A state to be set for this light.
   */
  void setState(UpdateLight state);
}
