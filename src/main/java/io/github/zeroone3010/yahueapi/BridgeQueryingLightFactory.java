package io.github.zeroone3010.yahueapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zeroone3010.yahueapi.domain.Root;

import java.net.MalformedURLException;
import java.net.URL;

final class BridgeQueryingLightFactory implements LightFactory {
  static LightFactory INSTANCE = new BridgeQueryingLightFactory();

  private BridgeQueryingLightFactory() { /* prevent */ }

  @Override
  public Light buildLight(final String lightId, final Root root, final String bridgeUri,
                          final ObjectMapper objectMapper) {
    try {
      return new LightImpl(lightId,
          root.getLights().get(lightId),
          new URL(bridgeUri + "lights/" + lightId),
          objectMapper);
    } catch (final MalformedURLException e) {
      throw new HueApiException(e);
    }
  }
}
