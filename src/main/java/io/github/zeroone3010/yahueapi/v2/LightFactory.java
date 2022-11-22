package io.github.zeroone3010.yahueapi.v2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zeroone3010.yahueapi.HueApiException;
import io.github.zeroone3010.yahueapi.v2.domain.GroupedLightResource;
import io.github.zeroone3010.yahueapi.v2.domain.GroupedLightResourceRoot;
import io.github.zeroone3010.yahueapi.v2.domain.LightResource;
import io.github.zeroone3010.yahueapi.v2.domain.LightResourceRoot;
import io.github.zeroone3010.yahueapi.v2.domain.update.UpdateLight;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public class LightFactory {

  private final Hue hue;
  private final ObjectMapper objectMapper;

  public LightFactory(final Hue hue, final ObjectMapper objectMapper) {
    this.hue = hue;
    this.objectMapper = objectMapper;
  }

  public LightImpl buildLight(final LightResource resource, final URL bridgeUrl) {
    try {
      final UUID id = resource.getId();
      final URL url = new URL(bridgeUrl, "/clip/v2/resource/light/" + id);
      return new LightImpl(
          id,
          resource,
          createStateProvider(url),
          stateSetter(url)
      );
    } catch (final MalformedURLException e) {
      throw new HueApiException(e);
    }
  }

  public GroupedLightImpl buildGroupedLight(final GroupedLightResource resource, final URL bridgeUrl) {
    try {
      final UUID id = resource.getId();
      final URL url = new URL(bridgeUrl, "/clip/v2/resource/grouped_light/" + id);
      return new GroupedLightImpl(
          id,
          resource,
          createGroupedLightStateProvider(url),
          stateSetter(url)
      );
    } catch (final MalformedURLException e) {
      throw new HueApiException(e);
    }
  }

  private Supplier<LightResource> createStateProvider(final URL url) {
    return () -> {
      try (final InputStream inputStream = hue.getUrlConnection(url).getInputStream()) {
        final LightResource lightResource = objectMapper.readValue(inputStream, LightResourceRoot.class).getData().get(0);
        return lightResource;
      } catch (final IOException e) {
        throw new HueApiException(e);
      }
    };
  }

  private Supplier<GroupedLightResource> createGroupedLightStateProvider(final URL url) {
    return () -> {
      try (final InputStream inputStream = hue.getUrlConnection(url).getInputStream()) {
        final GroupedLightResource lightResource = objectMapper.readValue(inputStream, GroupedLightResourceRoot.class).getData().get(0);
        return lightResource;
      } catch (final IOException e) {
        throw new HueApiException(e);
      }
    };
  }

  private Function<UpdateLight, String> stateSetter(final URL url) {
    return state -> {
      final String body;
      try {
        body = objectMapper.writeValueAsString(state);
      } catch (final JsonProcessingException e) {
        throw new HueApiException(e);
      }
      return HttpUtil.put(hue, url, "", body);
    };
  }
}
