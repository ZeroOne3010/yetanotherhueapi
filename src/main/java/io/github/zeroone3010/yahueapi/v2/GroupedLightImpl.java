package io.github.zeroone3010.yahueapi.v2;

import io.github.zeroone3010.yahueapi.HueApiException;
import io.github.zeroone3010.yahueapi.v2.domain.GroupedLightResource;
import io.github.zeroone3010.yahueapi.v2.domain.update.On;
import io.github.zeroone3010.yahueapi.v2.domain.update.UpdateLight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public final class GroupedLightImpl implements GroupedLight {
  private static final Logger logger = LoggerFactory.getLogger("io.github.zeroone3010.yahueapi");

  private final UUID id;
  private final Supplier<GroupedLightResource> stateProvider;
  private final Function<UpdateLight, String> stateSetter;

  public GroupedLightImpl(final UUID id,
                          final GroupedLightResource light,
                          final Supplier<GroupedLightResource> stateProvider,
                          final Function<UpdateLight, String> stateSetter) {
    this.id = id;
    if (light == null) {
      throw new HueApiException("GroupedLight " + id + " cannot be found.");
    }
    this.stateProvider = stateProvider;
    this.stateSetter = stateSetter;
  }

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public void turnOn() {
    final UpdateLight newState = new UpdateLight();
    newState.setOn(new On(true));
    stateSetter.apply(newState);
  }

  @Override
  public void turnOff() {
    final UpdateLight newState = new UpdateLight();
    newState.setOn(new On(false));
    System.out.println(stateSetter.apply(newState));
  }

  @Override
  public boolean isOn() {
    return getLightState().getOn().isOn();
  }

  private GroupedLightResource getLightState() {
    final GroupedLightResource state = stateProvider.get();
    logger.trace(state.toString());
    return state;
  }

  @Override
  public void setBrightness(final int brightness) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public void setState(final UpdateLight state) {
    final String result = stateSetter.apply(state);
    logger.trace(result);
  }

  @Override
  public String toString() {
    return "GroupedLight{" +
        "id='" + id + '\'' +
        '}';
  }
}
