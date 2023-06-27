package io.github.zeroone3010.yahueapi.v2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zeroone3010.yahueapi.HueApiException;
import io.github.zeroone3010.yahueapi.v2.domain.DeviceResource;
import io.github.zeroone3010.yahueapi.v2.domain.GroupResource;
import io.github.zeroone3010.yahueapi.v2.domain.GroupedLightResource;
import io.github.zeroone3010.yahueapi.v2.domain.GroupedLightResourceRoot;
import io.github.zeroone3010.yahueapi.v2.domain.Resource;
import io.github.zeroone3010.yahueapi.v2.domain.ResourceIdentifier;
import io.github.zeroone3010.yahueapi.v2.domain.SceneResource;
import io.github.zeroone3010.yahueapi.v2.domain.update.SceneActivation;
import io.github.zeroone3010.yahueapi.v2.domain.update.UpdateLight;
import io.github.zeroone3010.yahueapi.v2.domain.update.UpdateRoom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static io.github.zeroone3010.yahueapi.v2.domain.ResourceType.DEVICE;
import static io.github.zeroone3010.yahueapi.v2.domain.ResourceType.GROUPED_LIGHT;
import static io.github.zeroone3010.yahueapi.v2.domain.ResourceType.LIGHT;
import static io.github.zeroone3010.yahueapi.v2.domain.ResourceType.SCENE;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class GroupFactory {
  private static final Logger logger = LoggerFactory.getLogger(GroupFactory.class);

  private final Hue hue;
  private final ObjectMapper objectMapper;

  public GroupFactory(final Hue hue, final ObjectMapper objectMapper) {
    this.hue = hue;
    this.objectMapper = objectMapper;
  }

  public GroupImpl buildGroup(final GroupResource groupResource,
                              final Map<UUID, Resource> allResources) {
    final Supplier<Collection<Light>> lightProvider = () -> {
      final Set<Light> deviceLights = groupResource.getChildren().stream()
          .filter(r -> r.getResourceType() == DEVICE)
          .map(ResourceIdentifier::getResourceId)
          .map(hue::getResource)
          .map(r -> (DeviceResource) r)
          .flatMap(r -> r.getServices().stream())
          .filter(s -> s.getResourceType() == LIGHT)
          .map(light -> hue.getLights().get(light.getResourceId()))
          .collect(Collectors.toSet());
      final Set<Light> childLights = groupResource.getChildren().stream()
          .filter(r -> r.getResourceType() == LIGHT)
          .map(ResourceIdentifier::getResourceId)
          .map(id -> hue.getLights().get(id))
          .collect(Collectors.toSet());
      final Set<Light> result = new HashSet<>(deviceLights);
      result.addAll(childLights);
      return result;
    };
    final List<Scene> scenes = allResources.values().stream()
        .filter(r -> r.getType() == SCENE)
        .map(r -> (SceneResource) r)
        .filter(sceneResource -> Objects.equals(
            groupResource.getId(),
            sceneResource.getGroup().getResourceId()
        ))
        .map(sceneResource -> new SceneImpl(
            sceneResource.getId(),
            sceneResource.getMetadata().getName(),
            sceneStateSetter(sceneResource.getId())
        ))
        .collect(toList());
    return new GroupImpl(groupResource.getId(),
        groupResource.getType(),
        groupResource.getMetadata().getName(),
        scenes,
        lightProvider,
        createStateProvider(groupResource),
        stateSetter(groupResource),
        groupLightsSetter(groupResource)
    );
  }

  private Supplier<String> sceneStateSetter(final UUID sceneId) {
    return () -> {
      try {
        final String urlPath = resolveUrlPath(sceneId);
        final String body = objectMapper.writeValueAsString(new SceneActivation());
        return HttpUtil.put(hue, hue.getResourceUrl(), urlPath, body);
      } catch (final Exception e) {
        throw new HueApiException(e);
      }
    };
  }

  private Supplier<GroupedLightResource> createStateProvider(final GroupResource groupResource) {
    return () -> {
      final String urlPath = resolveUrlPath(groupResource);
      try (final InputStream inputStream = hue.getUrlConnection(urlPath).getInputStream()) {
        return objectMapper.readValue(inputStream, GroupedLightResourceRoot.class).getData().get(0);
      } catch (final IOException e) {
        throw new HueApiException(e);
      }
    };
  }

  private Function<Collection<Light>, String> groupLightsSetter(final GroupResource groupResource) {
    return allLights -> {
      final UpdateRoom roomResource = new UpdateRoom();
      final List<ResourceIdentifier> allLightResourceIdentifiers = allLights.stream()
          .map(Light::getOwnerId)
          .map(hue::getResource)
          .map(Resource::identifier)
          .collect(toList());
      roomResource.setChildren(allLightResourceIdentifiers);
      try {
        final String urlPath = resolveRoomUrlPath(groupResource);
        final String body = objectMapper.writeValueAsString(roomResource);
        return HttpUtil.put(hue, hue.getResourceUrl(), urlPath, body);
      } catch (final JsonProcessingException e) {
        throw new HueApiException(e);
      }
    };
  }

  private Function<UpdateLight, String> stateSetter(final GroupResource groupResource) {
    return state -> {
      final String urlPath = resolveUrlPath(groupResource);
      try {
        final String body = objectMapper.writeValueAsString(state);
        return HttpUtil.put(hue, hue.getResourceUrl(), urlPath, body);
      } catch (final Exception e) {
        throw new HueApiException(e);
      }
    };
  }

  private static String resolveUrlPath(final GroupResource groupResource) {
    return groupResource.getServices()
        .stream()
        .filter(service -> service.getResourceType() == GROUPED_LIGHT)
        .map(ResourceIdentifier::getResourceId)
        .findFirst()
        .map(uuid -> "/grouped_light/" + uuid)
        .orElse(null);
  }

  private String resolveRoomUrlPath(final GroupResource groupResource) {
    return "/room/" + groupResource.getId();
  }

  private static String resolveUrlPath(final UUID sceneId) {
    return "/scene/" + sceneId;
  }
}
