package com.github.zeroone3010.yahueapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zeroone3010.yahueapi.domain.Sensor;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.Map;
import java.util.logging.Logger;

final class TemperatureSensorImpl extends BasicSensor implements ITemperatureSensor {
  private static final Logger logger = Logger.getLogger("MotionSensorImpl");

  TemperatureSensorImpl(final String id, final Sensor sensor, final URL url, final ObjectMapper objectMapper) {
    super(id, sensor, url, objectMapper);
  }

  @Override
  public String toString() {
    return "TemperatureSensor{" +
        "id='" + super.id + '\'' +
        ", name='" + super.name + '\'' +
        ", type=" + super.type +
        '}';
  }

  @Override
  public BigDecimal getDegreesCelsius() {
    try {
      final Map<String, Object> state = super.objectMapper.readValue(baseUrl, Sensor.class).getState();
      logger.fine(state.toString());
      return convertCenticelsiusToCelsius((int) state.get("temperature"));
    } catch (final IOException e) {
      throw new HueApiException(e);
    }
  }

  private static BigDecimal convertCenticelsiusToCelsius(final int centicelsius) {
    return BigDecimal.valueOf(centicelsius).divide(BigDecimal.valueOf(100L), 2, RoundingMode.HALF_UP);
  }
}
