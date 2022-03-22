package io.github.zeroone3010.yahueapi;

import io.github.zeroone3010.yahueapi.domain.LightDto;
import io.github.zeroone3010.yahueapi.domain.LightState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;
import java.util.function.Supplier;

final class LightImpl implements Light {
  private static final Logger logger = LoggerFactory.getLogger(LightImpl.class);

  private final String id;
  private final String name;
  private final Supplier<LightState> stateProvider;
  private final Function<State, String> stateSetter;
  private final LightType type;
  private final Integer maxLumens;

  LightImpl(final String id, final LightDto light, final Supplier<LightState> stateProvider,
            final Function<State, String> stateSetter, final Integer maxLumens) {
    this.id = id;
    if (light == null) {
      throw new HueApiException("Light " + id + " cannot be found.");
    }
    this.name = light.getName();
    this.stateProvider = stateProvider;
    this.stateSetter = stateSetter;
    this.type = LightType.parseTypeString(light.getType());
    this.maxLumens = maxLumens;
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
  public void turnOn() {
    setState(((StateBuilderSteps.OnOffStep) State.builder()).on());
  }

  @Override
  public void turnOff() {
    setState(((StateBuilderSteps.OnOffStep) State.builder()).off());
  }

  @Override
  public boolean isOn() {
    return getLightState().isOn();
  }

  @Override
  public boolean isReachable() {
    return getLightState().isReachable();
  }

  private LightState getLightState() {
    final LightState state = stateProvider.get();
    logger.debug(state.toString());
    return state;
  }

  @Override
  public void setBrightness(final int brightness) {
    setState(((StateBuilderSteps.BrightnessStep) State.builder()).brightness(brightness).keepCurrentState());
  }

  @Override
  public void setState(final State state) {
    final String result = stateSetter.apply(state);
    logger.debug(result);
  }

  @Override
  public LightType getType() {
    return type;
  }

  @Override
  public State getState() {
    return State.build(getLightState());
  }

  @Override
  public Integer getMaxLumens() {
    return maxLumens;
  }

  @Override
  public String toString() {
    return "Light{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", type=" + type +
        ", maxLumens=" + maxLumens +
        '}';
  }
}
