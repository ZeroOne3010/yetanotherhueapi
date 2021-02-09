package io.github.zeroone3010.yahueapi;

import io.github.zeroone3010.yahueapi.domain.SensorDto;

import java.net.URL;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

final class DaylightSensorImpl extends BasicSensor implements DaylightSensor {
  private static final Logger logger = Logger.getLogger("DaylightSensor");

  DaylightSensorImpl(final String id, final SensorDto sensor, final URL url, final Supplier<Map<String, Object>> stateProvider) {
    super(id, sensor, url, stateProvider);
  }

  @Override
  public String toString() {
    return "DaylightSensor{" +
        "id='" + super.id + '\'' +
        ", name='" + super.name + '\'' +
        ", type=" + super.type +
        '}';
  }

  @Override
  public boolean isDaylightTime() {
    try {
      return readStateValue("daylight", Boolean.class);
    } catch (final NullPointerException npe) {
      logger.log(Level.WARNING, "It appears that the daylight sensor has not been configured.");
      return false;
    }
  }
}
