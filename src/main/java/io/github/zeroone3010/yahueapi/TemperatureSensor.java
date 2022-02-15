package io.github.zeroone3010.yahueapi;

import java.math.BigDecimal;

/**
 * A sensor measuring the room temperature. Most often found as an additional feature in a Philips Hue motion detector.
 */
public interface TemperatureSensor extends Sensor {
  /**
   * Returns the detected temperature in degrees Celsius.
   *
   * @return A {@code BigDecimal} with two decimal places, indicating the current temperature.
   * May also return {@code null} if the sensor is disabled in the Hue app.
   */
  BigDecimal getDegreesCelsius();
}
