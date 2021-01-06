package io.github.zeroone3010.yahueapi;

import java.time.ZonedDateTime;

public interface Sensor {
  /**
   * Returns the human readable name of the sensor as set by the user, e.g. "Living room switch".
   *
   * @return Name of the sensor.
   */
  String getName();

  /**
   * Returns the human readable name of the sensor product, e.g. "Hue motion sensor" or "Hue tap switch".
   *
   * @return Name of the sensor product. May be null.
   */
  String getProductName();

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
   * Returns the last time the sensor status has been updated, or null if it has not been updated.
   *
   * @return The time when the sensor status was last updated, or null.
   */
  ZonedDateTime getLastUpdated();
}
