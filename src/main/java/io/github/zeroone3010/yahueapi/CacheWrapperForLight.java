package io.github.zeroone3010.yahueapi;

import io.github.zeroone3010.yahueapi.domain.LightState;

public class CacheWrapperForLight implements Light {
  private final Hue hue;
  private final LightImpl light;
  private static final int DIMMABLE_LIGHT_COLOR_TEMPERATURE = 370;

  public CacheWrapperForLight(final Hue hue, final LightImpl light) {
    this.hue = hue;
    this.light = light;
  }

  @Override
  public String getName() {
    return light.getName();
  }

  @Override
  public void turnOn() {
    light.turnOn();
  }

  @Override
  public void turnOff() {
    light.turnOff();
  }

  @Override
  public boolean isOn() {
    if (hue.isCaching()) {
      return hue.getRaw().getLights().get(light.getId()).getState().isOn();
    }
    return light.isOn();
  }

  @Override
  public boolean isReachable() {
    if (hue.isCaching()) {
      return hue.getRaw().getLights().get(light.getId()).getState().isReachable();
    }
    return light.isReachable();
  }

  @Override
  public void setBrightness(int brightness) {
    light.setBrightness(brightness);
  }

  @Override
  public LightType getType() {
    return light.getType();
  }

  @Override
  public void setState(State state) {
    light.setState(state);
  }

  @Override
  public State getState() {
    if (hue.isCaching()) {
      final LightState state = hue.getRaw().getLights().get(light.getId()).getState();
      if (state.getColorMode() == null) {
        return State.builder().colorTemperatureInMireks(DIMMABLE_LIGHT_COLOR_TEMPERATURE).brightness(state.getBrightness()).on(state.isOn());
      }
      switch (state.getColorMode()) {
        case "xy":
          return State.builder().xy(state.getXy()).brightness(state.getBrightness()).on(state.isOn());
        case "ct":
          return State.builder().colorTemperatureInMireks(state.getCt()).brightness(state.getBrightness()).on(state.isOn());
        case "hs":
          return State.builder().hue(state.getHue()).saturation(state.getSaturation()).brightness(state.getBrightness()).on(state.isOn());
      }
      throw new HueApiException("Unknown color mode '" + state.getColorMode() + "'.");
    }
    return light.getState();
  }
}
