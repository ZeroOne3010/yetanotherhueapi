package io.github.zeroone3010.yahueapi.v2;

import io.github.zeroone3010.yahueapi.v2.domain.DeviceResource;
import io.github.zeroone3010.yahueapi.v2.domain.GroupResource;
import io.github.zeroone3010.yahueapi.v2.domain.ResourceIdentifier;

import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static io.github.zeroone3010.yahueapi.v2.domain.ResourceType.DEVICE;
import static io.github.zeroone3010.yahueapi.v2.domain.ResourceType.LIGHT;

final class GroupFactory {

  private final Hue hue;

  GroupFactory(final Hue hue) {
    this.hue = hue;
  }

  public GroupImpl buildGroup(final GroupResource groupResource,
                              final URL resourceUrl) {
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
        lightProvider);
  }
}
