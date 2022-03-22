package io.github.zeroone3010.yahueapi;

import io.github.zeroone3010.yahueapi.domain.SensorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.function.Supplier;

class BasicSensor implements Sensor {
  private static final Logger logger = LoggerFactory.getLogger(BasicSensor.class);
  private static final String NO_LAST_UPDATED_DATA = "none";

  protected final String id;
  protected final String name;
  protected final String productName;
  protected final URL baseUrl;
  protected final SensorType type;
  private final Supplier<Map<String, Object>> stateProvider;

  BasicSensor(final String id, final SensorDto sensor, final URL url, final Supplier<Map<String, Object>> stateProvider) {
    this.id = id;
    if (sensor == null) {
      throw new HueApiException("Sensor " + id + " cannot be found.");
    }
    this.name = sensor.getName();
    this.productName = sensor.getProductName();
    this.baseUrl = url;
    this.stateProvider = stateProvider;
    this.type = SensorType.parseTypeString(sensor.getType());
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getProductName() {
    return productName;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public SensorType getType() {
    return type;
  }

  @Override
  public ZonedDateTime getLastUpdated() {
    final String lastUpdated = readStateValue("lastupdated", String.class);
    if (NO_LAST_UPDATED_DATA.equals(lastUpdated)) {
      return null;
    }
    return TimeUtil.stringTimestampToZonedDateTime(lastUpdated);
  }

  protected <T> T readStateValue(final String stateValueKey, final Class<T> type) {
    final Map<String, Object> state = stateProvider.get();
      logger.debug(state.toString());
      return type.cast(state.get(stateValueKey));
  }

  @Override
  public String toString() {
    return "Sensor{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", type=" + type +
        '}';
  }
}
