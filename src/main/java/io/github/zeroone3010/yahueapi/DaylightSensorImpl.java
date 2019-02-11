package io.github.zeroone3010.yahueapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zeroone3010.yahueapi.domain.SensorDto;

import java.net.URL;

final class DaylightSensorImpl extends BasicSensor implements DaylightSensor {
  DaylightSensorImpl(final String id, final SensorDto sensor, final URL url, final ObjectMapper objectMapper) {
    super(id, sensor, url, objectMapper);
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
    return readStateValue("daylight", Boolean.class);
  }
}
