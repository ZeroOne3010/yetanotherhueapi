package io.github.zeroone3010.yahueapi.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * A data transfer object to hold the properties received from the Hue Bridge. Do not try to use this class
 * to change the state of a light, use {@link io.github.zeroone3010.yahueapi.State} instead.
 */
public class LightState {
  @SerializedName("on")
  private boolean on;
  @SerializedName("bri")
  private int brightness;
  @SerializedName("hue")
  private int hue;
  @SerializedName("sat")
  private int saturation;
  @SerializedName("effect")
  private String effect;
  @SerializedName("xy")
  private List<Float> xy;
  @SerializedName("ct")
  private int ct;
  @SerializedName("alert")
  private String alert;
  @SerializedName("colormode")
  private String colorMode;
  @SerializedName("mode")
  private String mode;
  @SerializedName("reachable")
  private boolean reachable;

  public boolean isOn() {
    return on;
  }

  /**
   * @param on
   * @deprecated The state cannot be changed with this class. Use {@link io.github.zeroone3010.yahueapi.State} instead.
   */
  public void setOn(final boolean on) {
    this.on = on;
  }

  public int getBrightness() {
    return brightness;
  }

  /**
   * @param brightness
   * @deprecated The state cannot be changed with this class. Use {@link io.github.zeroone3010.yahueapi.State} instead.
   */
  public void setBrightness(final int brightness) {
    this.brightness = brightness;
  }

  public int getHue() {
    return hue;
  }

  /**
   * @param hue
   * @deprecated The state cannot be changed with this class. Use {@link io.github.zeroone3010.yahueapi.State} instead.
   */
  public void setHue(final int hue) {
    this.hue = hue;
  }

  public int getSaturation() {
    return saturation;
  }

  /**
   * @param saturation
   * @deprecated The state cannot be changed with this class. Use {@link io.github.zeroone3010.yahueapi.State} instead.
   */
  public void setSaturation(final int saturation) {
    this.saturation = saturation;
  }

  public String getEffect() {
    return effect;
  }

  /**
   * @param effect
   * @deprecated The state cannot be changed with this class. Use {@link io.github.zeroone3010.yahueapi.State} instead.
   */
  public void setEffect(final String effect) {
    this.effect = effect;
  }

  public List<Float> getXy() {
    return xy;
  }

  /**
   * @param xy
   * @deprecated The state cannot be changed with this class. Use {@link io.github.zeroone3010.yahueapi.State} instead.
   */
  public void setXy(final List<Float> xy) {
    this.xy = xy;
  }

  public int getCt() {
    return ct;
  }

  /**
   * @param ct
   * @deprecated The state cannot be changed with this class. Use {@link io.github.zeroone3010.yahueapi.State} instead.
   */
  public void setCt(final int ct) {
    this.ct = ct;
  }

  public String getAlert() {
    return alert;
  }

  /**
   * @param alert
   * @deprecated The state cannot be changed with this class. Use {@link io.github.zeroone3010.yahueapi.State} instead.
   */
  public void setAlert(final String alert) {
    this.alert = alert;
  }

  public String getColorMode() {
    return colorMode;
  }

  /**
   * @param colorMode
   * @deprecated The state cannot be changed with this class. Use {@link io.github.zeroone3010.yahueapi.State} instead.
   */
  public void setColorMode(final String colorMode) {
    this.colorMode = colorMode;
  }

  public String getMode() {
    return mode;
  }

  /**
   * @param mode
   * @deprecated The state cannot be changed with this class. Use {@link io.github.zeroone3010.yahueapi.State} instead.
   */
  public void setMode(final String mode) {
    this.mode = mode;
  }

  public boolean isReachable() {
    return reachable;
  }

  /**
   * @param reachable
   * @deprecated The state cannot be changed with this class. Use {@link io.github.zeroone3010.yahueapi.State} instead.
   */
  public void setReachable(final boolean reachable) {
    this.reachable = reachable;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
