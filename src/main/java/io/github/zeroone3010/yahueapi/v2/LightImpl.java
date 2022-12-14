package io.github.zeroone3010.yahueapi.v2;

import io.github.zeroone3010.yahueapi.HueApiException;
import io.github.zeroone3010.yahueapi.v2.domain.LightResource;
import io.github.zeroone3010.yahueapi.v2.domain.update.Dimming;
import io.github.zeroone3010.yahueapi.v2.domain.update.UpdateLight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import static io.github.zeroone3010.yahueapi.v2.domain.update.On.OFF;
import static io.github.zeroone3010.yahueapi.v2.domain.update.On.ON;

public class LightImpl implements Light {
  private static final Logger logger = LoggerFactory.getLogger("io.github.zeroone3010.yahueapi");

  private final UUID id;
  private final String name;
  private final Supplier<LightResource> stateProvider;
  private final Function<UpdateLight, String> stateSetter;

  public LightImpl(final UUID id, final LightResource light, final Supplier<LightResource> stateProvider,
                   final Function<UpdateLight, String> stateSetter) {
    this.id = id;
    if (light == null) {
      throw new HueApiException("Light " + id + " cannot be found.");
    }
    this.name = light.getMetadata().getName();
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
  public void turnOn() {
    stateSetter.apply(new UpdateLight().setOn(ON));
  }

  @Override
  public void turnOff() {
    stateSetter.apply(new UpdateLight().setOn(OFF));
  }

  @Override
  public boolean isOn() {
    return getLightState().getOn().isOn();
  }

  private LightResource getLightState() {
    final LightResource state = stateProvider.get();
    logger.trace(state.toString());
    return state;
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
    logger.info("Update result: {}", result);
  }

  @Override
  public String toString() {
    return "Light{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        '}';
  }
}
