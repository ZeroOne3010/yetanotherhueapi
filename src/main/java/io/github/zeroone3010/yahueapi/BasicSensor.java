package io.github.zeroone3010.yahueapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zeroone3010.yahueapi.domain.SensorDto;

import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.logging.Logger;

class BasicSensor implements Sensor {
  private static final Logger logger = Logger.getLogger("SensorImpl");
  private static final String UTC_SUFFIX = "+00:00[UTC]";

  protected final String id;
  protected final String name;
  protected final URL baseUrl;
  protected final SensorType type;
  protected final ObjectMapper objectMapper;

  BasicSensor(final String id, final SensorDto sensor, final URL url, final ObjectMapper objectMapper) {
    this.id = id;
    if (sensor == null) {
      throw new HueApiException("Sensor " + id + " cannot be found.");
    }
    this.name = sensor.getName();
    this.baseUrl = url;
    this.objectMapper = objectMapper;
    this.type = SensorType.parseTypeString(sensor.getType());
  }

  @Override
  public String getName() {
    return name;
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
    try {
      final Map<String, Object> state = objectMapper.readValue(baseUrl, SensorDto.class).getState();
      logger.info(state.toString());
      return ZonedDateTime.parse(String.valueOf(state.get("lastupdated") + UTC_SUFFIX));
    } catch (final IOException e) {
      throw new HueApiException(e);
    }
  }

  protected <T> T readStateValue(final String stateValueKey, final Class<T> type) {
    try {
      final Map<String, Object> state = objectMapper.readValue(baseUrl, SensorDto.class).getState();
      logger.fine(state.toString());
      return type.cast(state.get(stateValueKey));
    } catch (final IOException e) {
      throw new HueApiException(e);
    }
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
