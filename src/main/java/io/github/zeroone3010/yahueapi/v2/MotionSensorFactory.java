package io.github.zeroone3010.yahueapi.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zeroone3010.yahueapi.HueApiException;
import io.github.zeroone3010.yahueapi.domain.SensorDto;
import io.github.zeroone3010.yahueapi.v2.domain.DeviceResource;
import io.github.zeroone3010.yahueapi.v2.domain.LightResource;
import io.github.zeroone3010.yahueapi.v2.domain.LightResourceRoot;
import io.github.zeroone3010.yahueapi.v2.domain.Motion;
import io.github.zeroone3010.yahueapi.v2.domain.MotionResourceRoot;
import io.github.zeroone3010.yahueapi.v2.domain.ResourceIdentifier;
import io.github.zeroone3010.yahueapi.v2.domain.ResourceType;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.function.Supplier;

public class MotionSensorFactory {
  private final Hue hue;
  private final ObjectMapper objectMapper;

  public MotionSensorFactory(final Hue hue, final ObjectMapper objectMapper) {
    this.hue = hue;
    this.objectMapper = objectMapper;
  }

  public MotionSensorImpl buildMotionSensor(final DeviceResource device, final URL bridgeUrl) {
    try {
      final String motionSensorId = device.getServices().stream()
          .filter(service -> service.getResourceType() == ResourceType.MOTION)
          .findFirst()
          .map(ResourceIdentifier::getResourceId)
          .map(UUID::toString)
          .orElse(null);
      final URL url = new URL(bridgeUrl, "/clip/v2/resource/motion/" + motionSensorId);
      final Supplier<Motion> stateProvider = createStateProvider(url);
      return new MotionSensorImpl(device.getId(), device.getMetadata().getName(), stateProvider);
    } catch (final MalformedURLException e) {
      throw new HueApiException(e);
    }
  }

  private Supplier<Motion> createStateProvider(final URL url) {
    return () -> {
      try (final InputStream inputStream = hue.getUrlConnection(url).getInputStream()) {
        final MotionResourceRoot motionResource = objectMapper.readValue(inputStream, MotionResourceRoot.class);
        return motionResource.getData().get(0).getMotion();
      } catch (final IOException e) {
        throw new HueApiException(e);
      }
    };
  }
}
