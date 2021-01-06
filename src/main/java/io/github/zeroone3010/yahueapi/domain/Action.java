package io.github.zeroone3010.yahueapi.domain;

import java.util.List;

public class Action {
  private boolean on;
  private int bri;
  private int hue;
  private int sat;
  private String effect;
  private List<Float> xy;
  private int ct;
  private String alert;
  private String colormode;

  public boolean isOn() {
    return on;
  }

  public int getBri() {
    return bri;
  }

  public int getHue() {
    return hue;
  }

  public int getSat() {
    return sat;
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

  public String getColormode() {
    return colormode;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
