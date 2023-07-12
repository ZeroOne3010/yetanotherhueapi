package io.github.zeroone3010.yahueapi.v2;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * A sensor measuring the room temperature. Most often found as an additional feature in a Philips Hue motion detector.
 */
public interface TemperatureSensor extends Device {
  /**
   * Returns the detected temperature in degrees Celsius.
   *
   * @return A {@code BigDecimal} with two decimal places, indicating the current temperature.
   * May also return {@code null} if the sensor is disabled in the Hue app.
   */
  BigDecimal getDegreesCelsius();

  /**
   * Returns the detected temperature in degrees Fahrenheit.
   *
   * @return A {@code BigDecimal} with two decimal places, indicating the current temperature.
   * May also return {@code null} if the sensor is disabled in the Hue app.
   */
  BigDecimal getDegreesFahrenheit();

  /**
   * The last time the temperature was updated.
   * @return Timestamp.
   */
  ZonedDateTime getLastChanged();
}
