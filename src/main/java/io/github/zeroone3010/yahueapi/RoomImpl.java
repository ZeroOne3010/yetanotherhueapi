package io.github.zeroone3010.yahueapi;

import io.github.zeroone3010.yahueapi.StateBuilderSteps.BrightnessStep;
import io.github.zeroone3010.yahueapi.domain.Group;
import io.github.zeroone3010.yahueapi.domain.GroupState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toSet;

final class RoomImpl implements Room {
  private static final Logger logger = LoggerFactory.getLogger(RoomImpl.class);

  private final String id;
  private final Supplier<Set<Light>> lights;
  private final Set<Scene> scenes;
  private final String name;
  private final Supplier<GroupState> stateProvider;
  private final Function<State, String> stateSetter;
  private final GroupType groupType;
  private final Function<Collection<String>, String> lightsSetter;

  RoomImpl(final String id,
           final Group group,
           final Supplier<Set<Light>> lights,
           final Set<Scene> scenes,
           final Supplier<GroupState> stateProvider,
           final Function<State, String> stateSetter,
           final Function<Collection<String>, String> lightsSetter) {
    this.id = id;
    this.stateProvider = stateProvider;
    this.stateSetter = stateSetter;
    this.lights = lights;
    this.scenes = scenes;
    this.name = group.getName();
    this.groupType = GroupType.parseTypeString(group.getType());
    this.lightsSetter = lightsSetter;
  }

  @Override
  public String getId() {
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
    return lights.get().stream()
        .filter(light -> Objects.equals(light.getName(), lightName))
        .findFirst();
  }

  @Override
  public boolean isAnyOn() {
    return getGroupState().isAnyOn();
  }

  @Override
  public boolean isAllOn() {
    return getGroupState().isAllOn();
  }

  @Override
  public void turnOn() {
    setState(((StateBuilderSteps.OnOffStep) State.builder()).on());
  }

  @Override
  public void turnOff() {
    setState(((StateBuilderSteps.OnOffStep) State.builder()).off());
  }

  @Override
  public void setState(final State state) {
    final String result = stateSetter.apply(state);
    logger.debug(result);
  }

  @Override
  public void setBrightness(final int brightness) {
    setState(((BrightnessStep) State.builder()).brightness(brightness).keepCurrentState());
  }

  @Override
  public GroupType getType() {
    return groupType;
  }

  @Override
  public Collection<Scene> getScenes() {
    return scenes;
  }

  @Override
  public Optional<Scene> getSceneByName(final String sceneName) {
    return scenes.stream()
        .filter(scene -> Objects.equals(scene.getName(), sceneName))
        .findFirst();
  }

  private GroupState getGroupState() {
    return stateProvider.get();
  }

  @Override
  public Collection<Light> addLight(final Light newLight) {
    if (newLight == null || newLight.getId() == null) {
      logger.warn("addLight: Given light was null. Doing nothing.");
      return getLights();
    }

    final Set<String> lightIds = new HashSet<>(getLights().stream().map(Light::getId).collect(toSet()));
    lightIds.add(newLight.getId());
    final String result = lightsSetter.apply(lightIds);
    logger.info(result);
    return getLights();
  }

  @Override
  public Collection<Light> removeLight(final Light lightToBeRemoved) {
    if (lightToBeRemoved == null || lightToBeRemoved.getId() == null) {
      logger.warn("removeLight: Given light was null. Doing nothing.");
      return getLights();
    }
    final Set<String> lightIds = new HashSet<>(getLights().stream().map(Light::getId).collect(toSet()));
    lightIds.remove(lightToBeRemoved.getId());
    final String result = lightsSetter.apply(lightIds);
    logger.debug(result);
    return getLights();
  }

  @Override
  public String toString() {
    return "Group{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", type='" + groupType + '\'' +
        '}';
  }
}
