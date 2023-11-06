package io.github.zeroone3010.yahueapi.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zeroone3010.yahueapi.HueApiException;
import io.github.zeroone3010.yahueapi.v2.domain.DeviceResource;
import io.github.zeroone3010.yahueapi.v2.domain.ResourceIdentifier;
import io.github.zeroone3010.yahueapi.v2.domain.ResourceType;
import io.github.zeroone3010.yahueapi.v2.domain.Temperature;
import io.github.zeroone3010.yahueapi.v2.domain.TemperatureResourceRoot;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.function.Supplier;

public class TemperatureSensorFactory {
  private final Hue hue;
  private final ObjectMapper objectMapper;

  public TemperatureSensorFactory(final Hue hue, final ObjectMapper objectMapper) {
    this.hue = hue;
    this.objectMapper = objectMapper;
  }

  public TemperatureSensorImpl buildTemperatureSensor(final DeviceResource device, final URL bridgeUrl) {
    try {
      final String sensorId = device.getServices().stream()
          .filter(service -> service.getResourceType() == ResourceType.TEMPERATURE)
          .findFirst()
          .map(ResourceIdentifier::getResourceId)
          .map(UUID::toString)
          .orElse(null);
      final URL url = new URL(bridgeUrl, "/clip/v2/resource/temperature/" + sensorId);
      final Supplier<Temperature> stateProvider = createStateProvider(url);
      return new TemperatureSensorImpl(device.getId(), device.getMetadata().getName(), stateProvider);
    } catch (final MalformedURLException e) {
      throw new HueApiException(e);
    }
  }

  private Supplier<Temperature> createStateProvider(final URL url) {
    return () -> {
      try (final InputStream inputStream = hue.getUrlConnection(url).getInputStream()) {
        final TemperatureResourceRoot resource = objectMapper.readValue(inputStream, TemperatureResourceRoot.class);
        return resource.getData().get(0).getTemperature();
      } catch (final IOException e) {
        throw new HueApiException(e);
      }
    };
  }
}
