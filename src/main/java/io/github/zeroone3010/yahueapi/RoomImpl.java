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

  private final Set<Light> lights;
  private final String name;
  private final Supplier<GroupState> stateProvider;
  private final Function<State, String> stateSetter;

  RoomImpl(final Group group,
           final Set<Light> lights,
           final Supplier<GroupState> stateProvider,
           final Function<State, String> stateSetter) {
    this.stateProvider = stateProvider;
    this.stateSetter = stateSetter;
    this.lights = lights;
    this.name = group.getName();
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
  public void setState(final State state) {
    final String result = stateSetter.apply(state);
    logger.fine(result);
  }

  @Override
  public void setBrightness(final int brightness) {
    setState(((BrightnessStep) State.builder()).brightness(brightness).keepCurrentState());
  }

  private GroupState getGroupState() {
    return stateProvider.get();
  }

  @Override
  public String toString() {
    return "Room{" +
        "name='" + name + '\'' +
        '}';
  }
}
