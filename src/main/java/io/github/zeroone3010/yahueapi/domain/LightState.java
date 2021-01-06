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

  public int getBrightness() {
    return brightness;
  }

  public int getHue() {
    return hue;
  }

  public int getSaturation() {
    return saturation;
  }

  public String getEffect() {
    return effect;
  }

  public List<Float> getXy() {
    return xy;
  }

  public int getCt() {
    return ct;
  }

  public String getAlert() {
    return alert;
  }

  public String getColorMode() {
    return colorMode;
  }

  public String getMode() {
    return mode;
  }

  public boolean isReachable() {
    return reachable;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
