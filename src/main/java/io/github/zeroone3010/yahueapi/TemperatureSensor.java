package io.github.zeroone3010.yahueapi;

import java.math.BigDecimal;

public interface TemperatureSensor extends Sensor {
  /**
   * Returns the detected temperature in degrees Celcius.
   *
   * @return A {@code BigDecimal} with two decimal places, indicating the current temperature.
   */
  BigDecimal getDegreesCelsius();
}
