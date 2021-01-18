package io.github.zeroone3010.yahueapi;

public interface AmbientLightSensor extends Sensor {

  /**
   * Returns the current light level in lux.
   *
   * @return A {@code int} indicating the current light level in lux.
   */
  int getLightLevel();

  /**
   * Tells whether the current light level is above light threshold.
   *
   * @return {@code true} if above light threshold, {@code false} if not.
   */
  boolean isDaylight();

  /**
   * Tells whether the current light level is below darkness threshold.
   *
   * @return {@code true} if below darkness threshold, {@code false} if not.
   */
  boolean isDark();
}
