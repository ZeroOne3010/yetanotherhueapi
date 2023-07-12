package io.github.zeroone3010.yahueapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.github.zeroone3010.yahueapi.StateBuilderSteps.AlertStep;
import io.github.zeroone3010.yahueapi.StateBuilderSteps.BrightnessStep;
import io.github.zeroone3010.yahueapi.StateBuilderSteps.BuildStep;
import io.github.zeroone3010.yahueapi.StateBuilderSteps.ColorStep;
import io.github.zeroone3010.yahueapi.StateBuilderSteps.ColorTemperatureStep;
import io.github.zeroone3010.yahueapi.StateBuilderSteps.HueStep;
import io.github.zeroone3010.yahueapi.StateBuilderSteps.InitialStep;
import io.github.zeroone3010.yahueapi.StateBuilderSteps.OnOffStep;
import io.github.zeroone3010.yahueapi.StateBuilderSteps.SaturationStep;
import io.github.zeroone3010.yahueapi.StateBuilderSteps.TransitionTimeStep;
import io.github.zeroone3010.yahueapi.StateBuilderSteps.XyStep;
import io.github.zeroone3010.yahueapi.domain.LightState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static io.github.zeroone3010.yahueapi.MathUtil.isInRange;
import static io.github.zeroone3010.yahueapi.XAndYAndBrightness.rgbToXy;

@JsonInclude(Include.NON_NULL)
public final class State {
  private static final Logger logger = LoggerFactory.getLogger(State.class);

  private static final int DIMMABLE_LIGHT_COLOR_TEMPERATURE = 370;

  /**
   * A state that causes a short blink to occur.
   */
  public static final State SHORT_ALERT = new State(AlertType.SHORT_ALERT);

  /**
   * A state that causes light(s) to blink for 15 seconds.
   */
  public static final State LONG_ALERT = new State(AlertType.LONG_ALERT);

  /**
   * A state that stops lights from blinking.
   */
  public static final State NO_ALERT = new State(AlertType.NONE);

  /**
   * A state that turns on the {@link EffectType#COLOR_LOOP} effect.
   */
  public static final State COLOR_LOOP_EFFECT = new State(EffectType.COLOR_LOOP);

  /**
   * A state that stops effects.
   */
  public static final State NO_EFFECTS = new State(EffectType.NONE);

  private final Boolean on;
  private final Integer hue;
  private final Integer sat;
  private final Integer bri;
  private final Integer ct;
  private final Integer transitiontime;
  private final List<Float> xy;
  private final String scene;
  private final AlertType alert;
  private final EffectType effect;

  private State(final Builder builder) {
    this.on = builder.on;
    this.bri = builder.bri;
    this.xy = builder.xy;
    this.hue = builder.hue;
    this.sat = builder.sat;
    this.ct = builder.ct;
    this.transitiontime = builder.transitionTime;
    this.scene = builder.scene;
    this.alert = builder.alert;
    this.effect = builder.effect;
  }

  State(final AlertType alertType) {
    this.on = null;
    this.bri = null;
    this.xy = null;
    this.hue = null;
    this.sat = null;
    this.ct = null;
    this.transitiontime = null;
    this.scene = null;
    this.alert = alertType;
    this.effect = null;
  }

  State(final EffectType effect) {
    this.on = null;
    this.bri = null;
    this.xy = null;
    this.hue = null;
    this.sat = null;
    this.ct = null;
    this.transitiontime = null;
    this.scene = null;
    this.alert = null;
    this.effect = effect;
  }

  public Boolean getOn() {
    return on;
  }

  public Integer getBri() {
    return bri;
  }

  public List<Float> getXy() {
    return xy;
  }

  public Integer getHue() {
    return hue;
  }

  public Integer getSat() {
    return sat;
  }

  public Integer getCt() {
    return ct;
  }

  public Integer getTransitiontime() {
    return transitiontime;
  }

  /**
   * The id of the scene that this state will activate. Note that the API does <strong>not</strong> populate
   * this value when you get the state of a light or a room. This means that unless you created this State object
   * manually, this value will always be {@code null}.
   *
   * @return Id of a scene, or {@code null} if this object was received from an actual {@code Light} object.
   * @since 1.3.0
   */
  public String getScene() {
    return scene;
  }

  /**
   * The latest alert command issued. Does not automatically reset to {@link AlertType#NONE} when the alert ends.
   *
   * @return Latest alert command issued.
   * @since 2.1.0
   */
  public AlertType getAlert() {
    return alert;
  }

  /**
   * The latest effect command issued. Does not automatically reset to {@link EffectType#NONE} when the effect ends.
   *
   * @return Latest effect command issued.
   * @since 2.3.0
   */
  public EffectType getEffect() {
    return effect;
  }

  public static InitialStep builder() {
    return new Builder();
  }

  static State build(final LightState state) {
    logger.debug(state.toString());
    final InitialStep builder = new State.Builder(AlertType.parseTypeString(state.getAlert()));
    if (state.getColorMode() == null) {
      return builder.colorTemperatureInMireks(DIMMABLE_LIGHT_COLOR_TEMPERATURE).brightness(state.getBrightness()).on(state.isOn());
    }
    switch (state.getColorMode()) {
      case "xy":
        return builder.xy(state.getXy()).brightness(state.getBrightness()).on(state.isOn());
      case "ct":
        return builder.colorTemperatureInMireks(state.getCt()).brightness(state.getBrightness()).on(state.isOn());
      case "hs":
        return builder.hue(state.getHue()).saturation(state.getSaturation()).brightness(state.getBrightness()).on(state.isOn());
    }
    throw new HueApiException("Unknown color mode '" + state.getColorMode() + "'.");
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final State state = (State) o;
    return Objects.equals(on, state.on) &&
        Objects.equals(hue, state.hue) &&
        Objects.equals(sat, state.sat) &&
        Objects.equals(bri, state.bri) &&
        Objects.equals(ct, state.ct) &&
        Objects.equals(xy, state.xy) &&
        Objects.equals(alert, state.alert) &&
        Objects.equals(effect, state.effect);
  }

  @Override
  public int hashCode() {
    return Objects.hash(on, hue, sat, bri, ct, xy, alert, effect);
  }


  public static final class Builder implements InitialStep, HueStep, SaturationStep, BrightnessStep, XyStep, ColorStep, ColorTemperatureStep, TransitionTimeStep, BuildStep, OnOffStep, AlertStep {
    private Boolean on;
    private Integer hue;
    private Integer sat;
    private Integer bri;
    private Integer ct;
    private Integer transitionTime;
    private List<Float> xy;
    private String scene;
    private AlertType alert;
    private EffectType effect;

    public Builder() {
    }

    Builder(AlertType alert) {
      this.alert = alert;
    }

    Builder(EffectType effect) {
      this.effect = effect;
    }

    @Override
    public SaturationStep hue(int hue) {
      this.hue = hue;
      return this;
    }

    @Override
    public BrightnessStep saturation(int saturation) {
      this.sat = saturation;
      return this;
    }

    @Override
    public BuildStep brightness(int brightness) {
      this.bri = brightness;
      return this;
    }

    @Override
    public BrightnessStep xy(List<Float> xy) {
      if (xy == null || xy.size() != 2 || !isInRange(xy.get(0), 0, 1) || !isInRange(xy.get(1), 0, 1)) {
        throw new IllegalArgumentException("The xy list must contain exactly 2 values, between 0 and 1.");
      }
      final List<Float> xyValues = new ArrayList<>();
      xyValues.addAll(xy);
      this.xy = Collections.unmodifiableList(xyValues);
      return this;
    }

    @Override
    public BuildStep color(Color color) {
      if (color == null) {
        throw new IllegalArgumentException("Color must not be null");
      }
      final XAndYAndBrightness xAndYAndBrightness = rgbToXy(color);
      this.xy = xAndYAndBrightness.getXY();
      this.bri = xAndYAndBrightness.getBrightness();
      return this;
    }

    @Override
    public BrightnessStep colorTemperatureInMireks(int colorTemperature) {
      this.ct = colorTemperature;
      return this;
    }

    @Override
    public OnOffStep transitionTime(int tenths) {
      this.transitionTime = tenths;
      return this;
    }

    @Override
    public State on(Boolean on) {
      this.on = on;
      return build();
    }

    @Override
    public BuildStep scene(String scene) {
      this.scene = scene;
      return this;
    }

    @Override
    public State alert(AlertType alert) {
      return new State(alert);
    }

    @Override
    public State effect(EffectType effect) {
      return new State(effect);
    }

    private State build() {
      return new State(this);
    }
  }

  @Override
  public String toString() {
    return "State{" +
        "on=" + on +
        ", hue=" + hue +
        ", sat=" + sat +
        ", bri=" + bri +
        ", ct=" + ct +
        ", transitiontime=" + transitiontime +
        ", xy=" + xy +
        ", scene='" + scene + '\'' +
        ", alert=" + alert +
        ", effect=" + effect +
        '}';
  }

}
