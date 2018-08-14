package com.github.zeroone3010.yahueapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zeroone3010.yahueapi.domain.SensorDto;

import java.net.URL;

final class MotionSensorImpl extends BasicSensor implements MotionSensor {

  MotionSensorImpl(final String id, final SensorDto sensor, final URL url, final ObjectMapper objectMapper) {
    super(id, sensor, url, objectMapper);
  }

  @Override
  public String toString() {
    return "MotionSensor{" +
        "id='" + super.id + '\'' +
        ", name='" + super.name + '\'' +
        ", type=" + super.type +
        '}';
  }

  @Override
  public boolean isPresence() {
    return readStateValue("presence", Boolean.class);
  }
}
