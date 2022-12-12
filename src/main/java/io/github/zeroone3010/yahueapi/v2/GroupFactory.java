package io.github.zeroone3010.yahueapi.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zeroone3010.yahueapi.HueApiException;
import io.github.zeroone3010.yahueapi.v2.domain.DeviceResource;
import io.github.zeroone3010.yahueapi.v2.domain.GroupResource;
import io.github.zeroone3010.yahueapi.v2.domain.GroupedLightResource;
import io.github.zeroone3010.yahueapi.v2.domain.GroupedLightResourceRoot;
import io.github.zeroone3010.yahueapi.v2.domain.ResourceIdentifier;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static io.github.zeroone3010.yahueapi.v2.domain.ResourceType.DEVICE;
import static io.github.zeroone3010.yahueapi.v2.domain.ResourceType.GROUPED_LIGHT;
import static io.github.zeroone3010.yahueapi.v2.domain.ResourceType.LIGHT;

public class GroupFactory {

  private final Hue hue;
  private final ObjectMapper objectMapper;

  public GroupFactory(final Hue hue, final ObjectMapper objectMapper) {
    this.hue = hue;
    this.objectMapper = objectMapper;
  }

  public GroupImpl buildGroup(final GroupResource groupResource) {
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
    return new GroupImpl(groupResource.getId(),
        groupResource.getType(),
        groupResource.getMetadata().getName(),
        lightProvider,
        createStateProvider(groupResource));
  }

  private Supplier<GroupedLightResource> createStateProvider(final GroupResource groupResource) {
    final UUID lightGroupId = groupResource.getServices()
        .stream()
        .filter(service -> service.getResourceType() == GROUPED_LIGHT)
        .map(ResourceIdentifier::getResourceId)
        .findFirst()
        .orElse(null);
    return () -> {
      final String urlPath = "/grouped_light/" + lightGroupId;
      try (final InputStream inputStream = hue.getUrlConnection(urlPath).getInputStream()) {
        return objectMapper.readValue(inputStream, GroupedLightResourceRoot.class).getData().get(0);
      } catch (final IOException e) {
        throw new HueApiException(e);
      }
    };
  }
}
