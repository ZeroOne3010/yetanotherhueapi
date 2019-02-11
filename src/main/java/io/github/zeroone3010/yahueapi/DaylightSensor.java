package io.github.zeroone3010.yahueapi;

public interface DaylightSensor extends Sensor {
  /**
   * Tells whether the current time is after sunrise but before sunset.
   *
   * @return {@code true} if it's daylight time, {@code false} if not.
   */
  boolean isDaylightTime();
}
