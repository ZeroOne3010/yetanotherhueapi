package com.github.zeroone3010.yahueapi;

import java.time.ZonedDateTime;

public interface Sensor {
  /**
   * Returns the human readable name of the sensor.
   *
   * @return Name of the sensor.
   */
  String getName();

  /**
   * Returns the id of the sensor, as assigned by the Bridge.
   *
   * @return The sensor id.
   */
  String getId();

  /**
   * Returns the type of the sensor.
   *
   * @return The type of the sensor as a {@code SensorType} enumeration value.
   */
  SensorType getType();

  /**
   * Returns the last time the sensor status has been updated.
   *
   * @return The time when the sensor status was last updated.
   */
  ZonedDateTime getLastUpdated();
}
