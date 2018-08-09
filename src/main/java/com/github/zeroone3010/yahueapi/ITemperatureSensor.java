package com.github.zeroone3010.yahueapi;

import java.math.BigDecimal;

public interface ITemperatureSensor extends ISensor {
  /**
   * Returns the detected temperature in degrees Celcius.
   *
   * @return A {@code BigDecimal} with two decimal places, indicating the current temperature.
   */
  BigDecimal getDegreesCelsius();
}
