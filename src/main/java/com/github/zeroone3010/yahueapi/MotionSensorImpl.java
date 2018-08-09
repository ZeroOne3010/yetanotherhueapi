package com.github.zeroone3010.yahueapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zeroone3010.yahueapi.domain.Sensor;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Logger;

final class MotionSensorImpl extends BasicSensor implements IMotionSensor {
  private static final Logger logger = Logger.getLogger("MotionSensorImpl");

  MotionSensorImpl(final String id, final Sensor sensor, final URL url, final ObjectMapper objectMapper) {
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
    try {
      final Map<String, Object> state = super.objectMapper.readValue(baseUrl, Sensor.class).getState();
      logger.fine(state.toString());
      return (boolean) state.get("presence");
    } catch (final IOException e) {
      throw new HueApiException(e);
    }
  }
}
