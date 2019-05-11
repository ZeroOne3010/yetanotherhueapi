package io.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * A data transfer object to hold the properties received from the Hue Bridge. Do not try to use this class
 * to change the state of a light, use {@link io.github.zeroone3010.yahueapi.State} instead.
 */
public class LightState {
  @JsonProperty("on")
  private boolean on;
  @JsonProperty("bri")
  private int brightness;
  @JsonProperty("hue")
  private int hue;
  @JsonProperty("sat")
  private int saturation;
  @JsonProperty("effect")
  private String effect;
  @JsonProperty("xy")
  private List<Float> xy;
  @JsonProperty("ct")
  private int ct;
  @JsonProperty("alert")
  private String alert;
  @JsonProperty("colormode")
  private String colorMode;
  @JsonProperty("mode")
  private String mode;
  @JsonProperty("reachable")
  private boolean reachable;

  public boolean isOn() {
    return on;
  }

  /**
   * @param on
   * @deprecated The state cannot be changed with this class. Use {@link io.github.zeroone3010.yahueapi.State} instead.
   */
  public void setOn(boolean on) {
    this.on = on;
  }

  public int getBrightness() {
    return brightness;
  }

  /**
   * @param brightness
   * @deprecated The state cannot be changed with this class. Use {@link io.github.zeroone3010.yahueapi.State} instead.
   */
  public void setBrightness(int brightness) {
    this.brightness = brightness;
  }

  public int getHue() {
    return hue;
  }

  /**
   * @param hue
   * @deprecated The state cannot be changed with this class. Use {@link io.github.zeroone3010.yahueapi.State} instead.
   */
  public void setHue(int hue) {
    this.hue = hue;
  }

  public int getSaturation() {
    return saturation;
  }

  /**
   * @param saturation
   * @deprecated The state cannot be changed with this class. Use {@link io.github.zeroone3010.yahueapi.State} instead.
   */
  public void setSaturation(int saturation) {
    this.saturation = saturation;
  }

  public String getEffect() {
    return effect;
  }

  /**
   * @param effect
   * @deprecated The state cannot be changed with this class. Use {@link io.github.zeroone3010.yahueapi.State} instead.
   */
  public void setEffect(String effect) {
    this.effect = effect;
  }

  public List<Float> getXy() {
    return xy;
  }

  /**
   * @param xy
   * @deprecated The state cannot be changed with this class. Use {@link io.github.zeroone3010.yahueapi.State} instead.
   */
  public void setXy(List<Float> xy) {
    this.xy = xy;
  }

  public int getCt() {
    return ct;
  }

  /**
   * @param ct
   * @deprecated The state cannot be changed with this class. Use {@link io.github.zeroone3010.yahueapi.State} instead.
   */
  public void setCt(int ct) {
    this.ct = ct;
  }

  public String getAlert() {
    return alert;
  }

  /**
   * @param alert
   * @deprecated The state cannot be changed with this class. Use {@link io.github.zeroone3010.yahueapi.State} instead.
   */
  public void setAlert(String alert) {
    this.alert = alert;
  }

  public String getColorMode() {
    return colorMode;
  }

  /**
   * @param colorMode
   * @deprecated The state cannot be changed with this class. Use {@link io.github.zeroone3010.yahueapi.State} instead.
   */
  public void setColorMode(String colorMode) {
    this.colorMode = colorMode;
  }

  public String getMode() {
    return mode;
  }

  /**
   * @param mode
   * @deprecated The state cannot be changed with this class. Use {@link io.github.zeroone3010.yahueapi.State} instead.
   */
  public void setMode(String mode) {
    this.mode = mode;
  }

  public boolean isReachable() {
    return reachable;
  }

  /**
   * @param reachable
   * @deprecated The state cannot be changed with this class. Use {@link io.github.zeroone3010.yahueapi.State} instead.
   */
  public void setReachable(boolean reachable) {
    this.reachable = reachable;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
