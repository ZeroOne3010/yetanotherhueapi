package io.github.zeroone3010.yahueapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zeroone3010.yahueapi.domain.LightDto;
import io.github.zeroone3010.yahueapi.domain.LightState;

import java.net.URL;

final class CacheWrapperForLight extends LightImpl {
  private final Hue hue;

  CacheWrapperForLight(final Hue hue,
                       final String id,
                       final LightDto light,
                       final URL url,
                       final ObjectMapper objectMapper) {
    super(id, light, url, objectMapper);
    this.hue = hue;
  }

  @Override
  protected LightState getLightState() {
    if (hue.isCaching()) {
      return hue.getRaw().getLights().get(super.getId()).getState();
    }
    return super.getLightState();
  }
}
