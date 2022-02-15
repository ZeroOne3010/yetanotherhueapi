package io.github.zeroone3010.yahueapi;

import io.github.zeroone3010.yahueapi.domain.SensorDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

final class TemperatureSensorImpl extends BasicSensor implements TemperatureSensor {
  private static final Logger logger = Logger.getLogger("io.github.zeroone3010.yahueapi");

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
    try {
      return convertCenticelsiusToCelsius(readStateValue("temperature", Integer.class));
    } catch (NullPointerException npe) {
      logger.log(Level.WARNING, "It appears that the temperature sensor may be disabled.");
      return null;
    }
  }

  private static BigDecimal convertCenticelsiusToCelsius(final int centicelsius) {
    return BigDecimal.valueOf(centicelsius).divide(BigDecimal.valueOf(100L), 2, RoundingMode.HALF_UP);
  }
}
