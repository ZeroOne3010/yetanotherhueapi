package com.github.zeroone3010.yahueapi;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@JsonInclude(Include.NON_NULL)
public final class State {
  private final Boolean on;
  private final Integer hue;
  private final Integer sat;
  private final Integer bri;
  private final Integer ct;
  private final List<Float> xy;

  /**
   * @param on         Set to {@code true} to turn on the light(s). Set to {@code false} to turn off the light(s).
   * @param brightness A value from {@code 0} (minimum brightness) to {@code 254} (maximum brightness).
   * @param colorTemperatureInMireks A value from {@code 153} (coldest white) to {@code 500} (warmest white).
   */
  public State(final boolean on,
               final int brightness,
               final int colorTemperatureInMireks) {
    this.on = on;
    this.bri = brightness;
    this.xy = null;
    this.hue = null;
    this.sat = null;
    this.ct = colorTemperatureInMireks;
  }

  /**
   * @param on       Set to {@code true} to turn on the light(s). Set to {@code false} to turn off the light(s).
   * @param hexColor A hexadecimal color value to be set for the light(s) -- for example, {@code 00FF00} for green.
   */
  public State(final boolean on,
               final String hexColor) {
    this(on, hexToColor(hexColor));
  }

  /**
   * @param on    Set to {@code true} to turn on the light(s). Set to {@code false} to turn off the light(s).
   * @param color A color to be set for the light(s).
   */
  public State(final boolean on, final Color color) {
    this.on = on;
    final XAndYAndBrightness xAndYAndBrightness = rgbToXy(color);
    this.xy = xAndYAndBrightness.getXY();
    this.bri = xAndYAndBrightness.getBrightness();
    this.hue = null;
    this.sat = null;
    this.ct = null;
  }

  /**
   * @param on         Set to {@code true} to turn on the light(s). Set to {@code false} to turn off the light(s).
   * @param xy         The x and y coordinates of the C.I.E. chromaticity diagram
   * @param brightness A value from {@code 0} (minimum brightness) to {@code 254} (maximum brightness).
   */
  public State(final boolean on, final List<Float> xy, final int brightness) {
    final List<Float> xyValues = new ArrayList<>(2);
    xyValues.addAll(xy);
    this.on = on;
    this.xy = Collections.unmodifiableList(xyValues);
    this.bri = brightness;
    this.hue = null;
    this.sat = null;
    this.ct = null;
  }

  /**
   * @param on         Set to {@code true} to turn on the light(s). Set to {@code false} to turn off the light(s).
   * @param hue        Hue, from {@code 0} to {@code 65280}.
   * @param saturation Saturation, from 0 to 254.
   * @param brightness A value from {@code 0} (minimum brightness) to {@code 254} (maximum brightness).
   */
  public State(final boolean on, final int hue, final int saturation, final int brightness) {
    this.on = on;
    this.xy = null;
    this.bri = brightness;
    this.hue = hue;
    this.sat = saturation;
    this.ct = null;
  }

  /**
   * Only to be used from the {@code HueCommandLineInterface} class by its ObjectMapper.
   */
  @JsonCreator
  State(@JsonProperty("on") final boolean on,
        @JsonProperty("color") final String hexColor,
        @JsonProperty("sat") final Integer saturation,
        @JsonProperty("bri") final Integer brightness,
        @JsonProperty("hue") final Integer hue,
        @JsonProperty("ct") final Integer colorTemperature) {
    if (colorTemperature != null) {
      this.on = on;
      this.bri = brightness;
      this.ct = colorTemperature;
      this.hue = null;
      this.sat = null;
      this.xy = null;
    } else if (hexColor != null && !hexColor.trim().isEmpty()) {
      this.on = on;
      final XAndYAndBrightness xAndYAndBrightness = rgbToXy(hexToColor(hexColor));
      this.xy = xAndYAndBrightness.getXY();
      this.bri = xAndYAndBrightness.getBrightness();
      this.hue = null;
      this.sat = null;
      this.ct = null;
    } else {
      this.on = on;
      this.xy = null;
      this.bri = brightness;
      this.hue = hue;
      this.sat = saturation;
      this.ct = null;
    }

  }

  private static Color hexToColor(final String hexColor) {
    return Optional.ofNullable(hexColor)
        .map(hex -> Integer.parseInt(hex, 16))
        .map(Color::new)
        .orElse(null);
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

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
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
      return ToStringBuilder.reflectionToString(this);
    }
  }
}
