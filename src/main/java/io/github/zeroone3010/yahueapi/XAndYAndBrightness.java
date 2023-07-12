package io.github.zeroone3010.yahueapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class XAndYAndBrightness {
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

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  /**
   * Brightness level.
   *
   * @return A number, max 255, for the old API (v1).
   */
  public int getBrightness() {
    return brightness;
  }

  /**
   * Brightness level.
   *
   * @return A number, max 100, for the new API (v2).
   */
  public int getBrightnessMax100() {
    return (int) ((brightness / 255d) * 100d);
  }

  public static XAndYAndBrightness rgbToXy(final Color color) {
    final float red = color.getRed();
    final float green = color.getGreen();
    final float blue = color.getBlue();
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

  public static double gammaCorrection(float component) {
    return (component > 0.04045f) ? Math.pow((component + 0.055f) / (1.0f + 0.055f), 2.4f) : (component / 12.92f);
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
