package io.github.zeroone3010.yahueapi;

import io.github.zeroone3010.yahueapi.domain.LightDto;
import io.github.zeroone3010.yahueapi.domain.LightState;

import java.net.URL;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;

class LightImpl implements Light {
  private static final Logger logger = Logger.getLogger("LightImpl");
  private static final String STATE_PATH = "/state";

  private final String id;
  private final String name;
  private final URL baseUrl;
  private final Supplier<LightState> stateProvider;
  private final Function<State, String> stateSetter;
  private final LightType type;

  LightImpl(final String id, final LightDto light, final URL url, final Supplier<LightState> stateProvider,
            final Function<State, String> stateSetter) {
    this.id = id;
    if (light == null) {
      throw new HueApiException("Light " + id + " cannot be found.");
    }
    this.name = light.getName();
    this.baseUrl = url;
    this.stateProvider = stateProvider;
    this.stateSetter = stateSetter;
    this.type = LightType.parseTypeString(light.getType());
  }

  protected String getId() {
    return id;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void turnOn() {
    final String body = "{\"on\":true}";
    final String result = HttpUtil.put(baseUrl, STATE_PATH, body);
    logger.fine(result);
  }

  @Override
  public void turnOff() {
    final String body = "{\"on\":false}";
    final String result = HttpUtil.put(baseUrl, STATE_PATH, body);
    logger.fine(result);
  }

  @Override
  public boolean isOn() {
    return getLightState().isOn();
  }

  @Override
  public boolean isReachable() {
    return getLightState().isReachable();
  }

  protected LightState getLightState() {
    final LightState state = stateProvider.get();
    logger.fine(state.toString());
    return state;
  }

  @Override
  public void setBrightness(final int brightness) {
    final String body = String.format("{\"bri\":%d}", brightness);
    final String result = HttpUtil.put(baseUrl, STATE_PATH, body);
    logger.fine(result);
  }

  @Override
  public void setState(final State state) {
    final String result = stateSetter.apply(state);
    logger.fine(result);
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
  public String toString() {
    return "Light{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", type=" + type +
        '}';
  }
}
