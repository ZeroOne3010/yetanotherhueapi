package io.github.zeroone3010.yahueapi;

import com.google.gson.Gson;
import io.github.zeroone3010.yahueapi.StateBuilderSteps.*;
import io.github.zeroone3010.yahueapi.domain.LightState;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.logging.Logger;

public final class State {
  private static final Logger logger = Logger.getLogger("State");

  private static final int DIMMABLE_LIGHT_COLOR_TEMPERATURE = 370;

  private final Boolean on;
  private final Integer hue;
  private final Integer sat;
  private final Integer bri;
  private final Integer ct;
  private final Integer transitiontime;
  private final List<Float> xy;
  private final String scene;

  private State(final Builder builder) {
    this.on = builder.on;
    this.bri = builder.bri;
    this.xy = builder.xy;
    this.hue = builder.hue;
    this.sat = builder.sat;
    this.ct = builder.ct;
    this.transitiontime = builder.transitionTime;
    this.scene = builder.scene;
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

  public String getScene() {
    return scene;
  }

  public static InitialStep builder() {
    return new Builder();
  }

  static State build(final LightState state) {
    logger.fine(state.toString());
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

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final State state = (State) o;
    return Objects.equals(on, state.on) &&
        Objects.equals(hue, state.hue) &&
        Objects.equals(sat, state.sat) &&
        Objects.equals(bri, state.bri) &&
        Objects.equals(ct, state.ct) &&
        Objects.equals(xy, state.xy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(on, hue, sat, bri, ct, xy);
  }


  public static final class Builder implements InitialStep, HueStep, SaturationStep, BrightnessStep, XyStep, ColorStep, ColorTemperatureStep, TransitionTimeStep, BuildStep, OnOffStep {
    private Boolean on;
    private Integer hue;
    private Integer sat;
    private Integer bri;
    private Integer ct;
    private Integer transitionTime;
    private List<Float> xy;
    private String scene;

    @Override
    public SaturationStep hue(final int hue) {
      this.hue = hue;
      return this;
    }

    @Override
    public BrightnessStep saturation(final int saturation) {
      this.sat = saturation;
      return this;
    }

    @Override
    public BuildStep brightness(final int brightness) {
      this.bri = brightness;
      return this;
    }

    @Override
    public BrightnessStep xy(final List<Float> xy) {
      if (xy == null || xy.size() != 2 || !isInRange(xy.get(0), 0, 1) || !isInRange(xy.get(1), 0, 1)) {
        throw new IllegalArgumentException("The xy list must contain exactly 2 values, between 0 and 1.");
      }
      final List<Float> xyValues = new ArrayList<>();
      xyValues.addAll(xy);
      this.xy = Collections.unmodifiableList(xyValues);
      return this;
    }

    private boolean isInRange(final Float value, final float min, final float max) {
      return value != null && !(value < min) && !(value > max);
    }

    @Override
    public BuildStep color(final Color color) {
      if (color == null) {
        throw new IllegalArgumentException("Color must not be null");
      }
      final XAndYAndBrightness xAndYAndBrightness = rgbToXy(color);
      this.xy = xAndYAndBrightness.getXY();
      this.bri = xAndYAndBrightness.getBrightness();
      return this;
    }

    @Override
    public BuildStep color(final String color) {
      return color(hexToColor(color));
    }

    @Override
    public BrightnessStep colorTemperatureInMireks(final int colorTemperature) {
      this.ct = colorTemperature;
      return this;
    }

    @Override
    public OnOffStep transitionTime(final int tenths) {
      this.transitionTime = tenths;
      return this;
    }

    @Override
    public State on(final Boolean on) {
      this.on = on;
      return build();
    }

    @Override
    public BuildStep scene(final String scene) {
      this.scene = scene;
      return this;
    }

    private State build() {
      return new State(this);
    }

    private static Color hexToColor(final String hexColor) {
      return Optional.ofNullable(hexColor)
          .map(hex -> Integer.parseInt(hex, 16))
          .map(Color::new)
          .orElse(null);
    }

    private static XAndYAndBrightness rgbToXy(final Color color) {
      final float red = color.getRed() / 255f;
      final float green = color.getGreen() / 255f;
      final float blue = color.getBlue() / 255f;
      final double r = gammaCorrection(red);
      final double g = gammaCorrection(green);
      final double b = gammaCorrection(blue);
      final double rgbX = r * 0.664511f + g * 0.154324f + b * 0.162028f;
      final double rgbY = r * 0.283881f + g * 0.668433f + b * 0.047685f;
      final double rgbZ = r * 0.000088f + g * 0.072310f + b * 0.986039f;
      final float x = (float) (rgbX / (rgbX + rgbY + rgbZ));
      final float y = (float) (rgbY / (rgbX + rgbY + rgbZ));
      return new XAndYAndBrightness(x, y, (int) (rgbY * 255f));
    }

    private static double gammaCorrection(final float component) {
      return (component > 0.04045f) ? Math.pow((component + 0.055f) / (1.0f + 0.055f), 2.4f) : (component / 12.92f);
    }
  }


  private static final class XAndYAndBrightness {
    final float x;
    final float y;
    final int brightness;

    XAndYAndBrightness(final float x, final float y, final int brightness) {
      this.x = x;
      this.y = y;
      this.brightness = brightness;
    }

    List<Float> getXY() {
      final List<Float> xyColor = new ArrayList<>();
      xyColor.add(this.x);
      xyColor.add(this.y);
      return xyColor;
    }

    int getBrightness() {
      return brightness;
    }

    @Override
    public String toString() {
      return new Gson().toJson(this);
    }
  }

}
