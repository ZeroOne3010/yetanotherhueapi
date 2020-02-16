package io.github.zeroone3010.yahueapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zeroone3010.yahueapi.domain.Group;
import io.github.zeroone3010.yahueapi.domain.GroupState;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toSet;

final class RoomFactory {
  private static final String ACTION_PATH = "/action";

  private final Hue hue;
  private final ObjectMapper objectMapper;
  private final String bridgeUri;

  RoomFactory(final Hue hue, final ObjectMapper objectMapper, final String bridgeUri) {
    this.hue = hue;
    this.objectMapper = objectMapper;
    this.bridgeUri = bridgeUri;
  }

  Room buildRoom(final String groupId,
                 final Group group,
                 final Map<String, io.github.zeroone3010.yahueapi.domain.Scene> scenes) {
    final Set<Light> lights = group.getLights().stream()
        .map(hue::getLightById)
        .collect(toSet());
    try {
      final URL url = new URL(bridgeUri + "groups/" + groupId);
      final Function<State, String> stateSetter = stateSetter(url);
      return new RoomImpl(
          groupId,
          group,
          lights,
          buildScenes(scenes, stateSetter),
          createStateProvider(url, groupId),
          stateSetter);
    } catch (final MalformedURLException e) {
      throw new HueApiException(e);
    }
  }

  private Set<Scene> buildScenes(final Map<String, io.github.zeroone3010.yahueapi.domain.Scene> scenes,
                                 final Function<State, String> stateSetter) {
    return scenes.entrySet().stream()
        .map(e -> new SceneImpl(e.getKey(), e.getValue().getName(), stateSetter))
        .collect(toSet());
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
