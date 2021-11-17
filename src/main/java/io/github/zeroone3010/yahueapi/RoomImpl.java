package io.github.zeroone3010.yahueapi;

import io.github.zeroone3010.yahueapi.StateBuilderSteps.BrightnessStep;
import io.github.zeroone3010.yahueapi.domain.Group;
import io.github.zeroone3010.yahueapi.domain.GroupState;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;

final class RoomImpl implements Room {
  private static final Logger logger = Logger.getLogger("RoomImpl");

  private final String id;
  private final Set<Light> lights;
  private final Set<Scene> scenes;
  private final String name;
  private final Supplier<GroupState> stateProvider;
  private final Function<State, String> stateSetter;
  private final GroupType groupType;

  RoomImpl(final String id,
           final Group group,
           final Set<Light> lights,
           final Set<Scene> scenes,
           final Supplier<GroupState> stateProvider,
           final Function<State, String> stateSetter) {
    this.id = id;
    this.stateProvider = stateProvider;
    this.stateSetter = stateSetter;
    this.lights = lights;
    this.scenes = scenes;
    this.name = group.getName();
    this.groupType = GroupType.parseTypeString(group.getType());
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
    return lights;
  }

  @Override
  public Optional<Light> getLightByName(final String lightName) {
    return lights.stream()
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
    logger.fine(result);
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
  public String toString() {
    return "Group{" +
        "name='" + name + '\'' +
        "type='" + groupType + '\'' +
        '}';
  }
}
