package io.github.zeroone3010.yahueapi;

import io.github.zeroone3010.yahueapi.domain.LightState;

public class CacheWrapperForLight implements Light {
  private final Hue hue;
  private final LightImpl light;

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
      return State.build(state);
    }
    return light.getState();
  }
}
