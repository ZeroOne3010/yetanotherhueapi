package io.github.zeroone3010.yahueapi;

import com.google.gson.Gson;
import io.github.zeroone3010.yahueapi.domain.SensorDto;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.function.Supplier;

final class SensorFactory {
  private final Hue hue;
  private final Gson objectMapper;

  SensorFactory(final Hue hue, final Gson objectMapper) {
    this.hue = hue;
    this.objectMapper = objectMapper;
  }

  Sensor buildSensor(final String id, final SensorDto sensor, final String bridgeUri) {
    if (sensor == null) {
      throw new HueApiException("Sensor " + id + " cannot be found.");
    }

    final URL url = buildSensorUrl(bridgeUri, id);

    final SensorType type = SensorType.parseTypeString(sensor.getType());
    final Supplier<Map<String, Object>> stateProvider = createStateProvider(url, id);
    switch (type) {
      case MOTION:
        return new MotionSensorImpl(id, sensor, url, stateProvider);
      case TEMPERATURE:
        return new TemperatureSensorImpl(id, sensor, url, stateProvider);
      case DAYLIGHT:
        return new DaylightSensorImpl(id, sensor, url, stateProvider);
      case DIMMER_SWITCH:
        return new DimmerSwitchImpl(id, sensor, url, stateProvider);
      default:
        return new BasicSensor(id, sensor, url, stateProvider);
    }
  }

  private Supplier<Map<String, Object>> createStateProvider(final URL url, final String id) {
    return () -> {
      if (hue.isCaching()) {
        return hue.getRaw().getSensors().get(id).getState();
      }
      try {
        return objectMapper.fromJson(HttpUtil.getString(url), SensorDto.class).getState();
      } catch (final IOException e) {
        throw new HueApiException(e);
      }
    };
  }

  private static URL buildSensorUrl(final String bridgeUri, final String sensorId) {
    try {
      return new URL(bridgeUri + "sensors/" + sensorId);
    } catch (final MalformedURLException e) {
      throw new HueApiException(e);
    }
  }

}
