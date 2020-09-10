package io.github.zeroone3010.yahueapi;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import io.github.zeroone3010.yahueapi.domain.LightDto;
import io.github.zeroone3010.yahueapi.domain.LightState;
import io.github.zeroone3010.yahueapi.domain.Root;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Function;
import java.util.function.Supplier;

final class LightFactory {
  private static final String ACTION_PATH = "/state";

  private final Hue hue;
  private final Gson objectMapper;

  LightFactory(final Hue hue, final Gson objectMapper) {
    this.hue = hue;
    this.objectMapper = objectMapper;
  }

  LightImpl buildLight(final String lightId, final Root root, final String bridgeUri) {
    try {
      final URL url = new URL(bridgeUri + "lights/" + lightId);
      return new LightImpl(
          lightId,
          root.getLights().get(lightId),
          createStateProvider(url, lightId),
          stateSetter(url));
    } catch (final MalformedURLException e) {
      throw new HueApiException(e);
    }
  }

  private Supplier<LightState> createStateProvider(final URL url,
                                                   final String id) {
    return () -> {
      if (hue.isCaching()) {
        return hue.getRaw().getLights().get(id).getState();
      }
      try {
        return objectMapper.fromJson(HttpUtil.getString(url), LightDto.class).getState();
      } catch (final IOException e) {
        throw new HueApiException(e);
      }
    };
  }

  private Function<State, String> stateSetter(final URL url) {
    return state -> {
      final String body;
      try {
        body = objectMapper.toJson(state);
      } catch (final JsonIOException e) {
        throw new HueApiException(e);
      }
      return HttpUtil.put(url, ACTION_PATH, body);
    };
  }
}
