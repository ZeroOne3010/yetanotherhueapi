package io.github.zeroone3010.yahueapi.v2;

import io.github.zeroone3010.yahueapi.v2.domain.update.UpdateLight;

import java.util.UUID;

/**
 * A group of lights as defined in the Bridge. This might, for example, be a ceiling fixture that
 * has multiple bulbs in it. Grouped lights unfortunately do not have human-readable names.
 */
public interface GroupedLight {

  /**
   * <p>Returns the technical id of the grouped light, as assigned by the Bridge.</p>
   *
   * @return Id of the light.
   * @since 3.0.0
   */
  UUID getId();

  /**
   * Turns the light on.
   */
  void turnOn();

  /**
   * Turns the light off.
   */
  void turnOff();

  /**
   * Queries the light state -- or returns the cached state if caching
   * has been enabled with {@link io.github.zeroone3010.yahueapi.Hue#setCaching(boolean)}.
   *
   * @return True if the light is on, false if it is off.
   */
  boolean isOn();

  /**
   * Sets the brightness of the light. If the light is off, does not turn it on, nor does {@code 0} turn it off.
   *
   * @param brightness A value from {@code 0} (minimum brightness) to {@code 254} (maximum brightness).
   */
  void setBrightness(int brightness);

  /**
   * Sets a state for the light.
   *
   * @param state A state to be set for this light.
   */
  void setState(UpdateLight state);
}
