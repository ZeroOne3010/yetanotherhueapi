package io.github.zeroone3010.yahueapi;

import io.github.zeroone3010.yahueapi.domain.SensorDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Logger;

final class TemperatureSensorImpl extends BasicSensor implements TemperatureSensor {
  private static final Logger logger = Logger.getLogger("MotionSensorImpl");

  TemperatureSensorImpl(final String id, final SensorDto sensor, final URL url, final Supplier<Map<String, Object>> stateProvider) {
    super(id, sensor, url, stateProvider);
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
    return convertCenticelsiusToCelsius(readStateValue("temperature", Double.class));
  }

  private static BigDecimal convertCenticelsiusToCelsius(final double centicelsius) {
    return BigDecimal.valueOf(centicelsius).divide(BigDecimal.valueOf(100L), 2, RoundingMode.HALF_UP);
  }
}
