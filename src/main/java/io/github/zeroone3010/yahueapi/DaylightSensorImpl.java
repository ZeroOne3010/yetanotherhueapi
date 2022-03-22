package io.github.zeroone3010.yahueapi;

import io.github.zeroone3010.yahueapi.domain.SensorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Map;
import java.util.function.Supplier;

final class DaylightSensorImpl extends BasicSensor implements DaylightSensor {
  private static final Logger logger = LoggerFactory.getLogger(DaylightSensorImpl.class);

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
      logger.warn("It appears that the daylight sensor has not been configured.");
      return false;
    }
  }
}
