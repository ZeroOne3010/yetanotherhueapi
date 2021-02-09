package io.github.zeroone3010.yahueapi;

/**
 * The daylight sensor is a virtual sensor. It calculates the presence of daylight based the user's home location,
 * i.e. their geographical coordinates. The user must set their home location in the advanced settings of the Hue app
 * for this sensor to work.
 */
public interface DaylightSensor extends Sensor {
  /**
   * Tells whether the current time is after sunrise but before sunset.
   *
   * @return {@code true} if it's daylight time, {@code false} if not.
   * Also returns {@code false} if the sensor has not been configured properly.
   */
  boolean isDaylightTime();
}
