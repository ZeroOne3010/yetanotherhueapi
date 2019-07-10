package io.github.zeroone3010.yahueapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zeroone3010.yahueapi.domain.Group;
import io.github.zeroone3010.yahueapi.domain.GroupState;
import io.github.zeroone3010.yahueapi.domain.Root;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toSet;

final class RoomFactory {
  private static final String ACTION_PATH = "/action";

  private final Hue hue;
  private final ObjectMapper objectMapper;
  private final String bridgeUri;
  private final LightFactory lightFactory;

  RoomFactory(final Hue hue, final ObjectMapper objectMapper, final String bridgeUri) {
    this.hue = hue;
    this.objectMapper = objectMapper;
    this.bridgeUri = bridgeUri;
    this.lightFactory = new LightFactory(hue, objectMapper);
  }

  Room buildRoom(final String groupId, final Group group, final Root root) {
    final Set<Light> lights = group.getLights().stream()
        .map(lightId -> buildLight(lightId, root))
        .collect(toSet());
    try {
      final URL url = new URL(bridgeUri + "groups/" + groupId);
      return new RoomImpl(
          group,
          lights,
          createStateProvider(url, groupId),
          stateSetter(url));
    } catch (final MalformedURLException e) {
      throw new HueApiException(e);
    }
  }

  private Light buildLight(final String lightId, final Root root) {
    return lightFactory.buildLight(lightId, root, bridgeUri);
  }

  private Supplier<GroupState> createStateProvider(final URL url,
                                                   final String id) {
    return () -> {
      if (hue.isCaching()) {
        return hue.getRaw().getGroups().get(id).getState();
      }
      try {
        return objectMapper.readValue(url, Group.class).getState();
      } catch (final IOException e) {
        throw new HueApiException(e);
      }
    };
  }

  private Function<State, String> stateSetter(final URL url) {
    return state -> {
      final String body;
      try {
        body = objectMapper.writeValueAsString(state);
      } catch (final JsonProcessingException e) {
        throw new HueApiException(e);
      }
      return HttpUtil.put(url, ACTION_PATH, body);
    };
  }
}
