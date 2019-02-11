package io.github.zeroone3010.yahueapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zeroone3010.yahueapi.domain.SensorDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.logging.Logger;

final class TemperatureSensorImpl extends BasicSensor implements TemperatureSensor {
  private static final Logger logger = Logger.getLogger("MotionSensorImpl");

  TemperatureSensorImpl(final String id, final SensorDto sensor, final URL url, final ObjectMapper objectMapper) {
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
    return convertCenticelsiusToCelsius(readStateValue("temperature", Integer.class));
  }

  private static BigDecimal convertCenticelsiusToCelsius(final int centicelsius) {
    return BigDecimal.valueOf(centicelsius).divide(BigDecimal.valueOf(100L), 2, RoundingMode.HALF_UP);
  }
}
