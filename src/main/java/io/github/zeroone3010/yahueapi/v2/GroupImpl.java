package io.github.zeroone3010.yahueapi.v2;

import io.github.zeroone3010.yahueapi.v2.domain.GroupedLightResource;
import io.github.zeroone3010.yahueapi.v2.domain.ResourceType;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class GroupImpl implements Group {

  private UUID id;
  private ResourceType type;
  private String name;
  private Supplier<Collection<Light>> lights;
  private Supplier<GroupedLightResource> stateProvider;

  public GroupImpl(final UUID id,
                   final ResourceType type,
                   final String name,
                   final Supplier<Collection<Light>> lights,
                   final Supplier<GroupedLightResource> stateProvider) {
    this.id = id;
    this.type = type;
    this.name = name;
    this.lights = lights;
    this.stateProvider = stateProvider;
  }

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Collection<Light> getLights() {
    return lights.get();
  }

  @Override
  public Optional<Light> getLightByName(final String lightName) {
    return lights.get().stream().filter(l -> Objects.equals(l.getName(), lightName)).findFirst();
  }

  @Override
  public ResourceType getType() {
    return type;
  }

  @Override
  public boolean isAnyOn() {
    return stateProvider.get().getOn().isOn();
  }
}
