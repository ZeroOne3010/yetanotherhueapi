package io.github.zeroone3010.yahueapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@JsonInclude(Include.NON_NULL)
public final class State {
  private final Boolean on;
  private final Integer hue;
  private final Integer sat;
  private final Integer bri;
  private final Integer ct;
  private final Integer transitiontime;
  private final List<Float> xy;

  private State(final Builder builder) {
    this.on = builder.on;
    this.bri = builder.bri;
    this.xy = builder.xy;
    this.hue = builder.hue;
    this.sat = builder.sat;
    this.ct = builder.ct;
    this.transitiontime = builder.transitionTime;
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

  public static InitialStep builder() {
    return new Builder();
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

    private boolean isInRange(final Float value, final float min, final float max) {
      return value != null && !(value < min) && !(value > max);
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
    public BuildStep color(String color) {
      return color(hexToColor(color));
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

    private static double gammaCorrection(float component) {
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
      try {
        return new ObjectMapper().writeValueAsString(this);
      } catch (final JsonProcessingException e) {
        throw new RuntimeException(e);
      }
    }
  }

}
