package io.github.zeroone3010.yahueapi.v2;

import io.github.zeroone3010.yahueapi.v2.domain.GroupedLightResource;
import io.github.zeroone3010.yahueapi.v2.domain.ResourceType;
import io.github.zeroone3010.yahueapi.v2.domain.update.UpdateLight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import static io.github.zeroone3010.yahueapi.v2.domain.update.On.OFF;
import static io.github.zeroone3010.yahueapi.v2.domain.update.On.ON;

public class GroupImpl implements Group {
  private static final Logger logger = LoggerFactory.getLogger("io.github.zeroone3010.yahueapi");

  private UUID id;
  private ResourceType type;
  private String name;
  private Supplier<Collection<Light>> lights;
  private Supplier<GroupedLightResource> stateProvider;
  private final Function<UpdateLight, String> stateSetter;


  public GroupImpl(final UUID id,
                   final ResourceType type,
                   final String name,
                   final Supplier<Collection<Light>> lights,
                   final Supplier<GroupedLightResource> stateProvider,
                   final Function<UpdateLight, String> stateSetter) {
    this.id = id;
    this.type = type;
    this.name = name;
    this.lights = lights;
    this.stateProvider = stateProvider;
    this.stateSetter = stateSetter;
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

  @Override
  public void turnOn() {
    stateSetter.apply(new UpdateLight().setOn(ON));
  }

  @Override
  public void turnOff() {
    stateSetter.apply(new UpdateLight().setOn(OFF));
  }

  @Override
  public void setBrightness(final int brightness) {
    setState(new UpdateState().brightness(brightness).getUpdateLight());
  }

  @Override
  public void setState(final UpdateState state) {
    setState(state.getUpdateLight());
  }

  private void setState(final UpdateLight state) {
    final String result = stateSetter.apply(state);
    logger.info("Group update result: {}", result);
  }
}
