package com.github.zeroone3010.yahueapi.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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

  public void setOn(boolean on) {
    this.on = on;
  }

  public int getBri() {
    return bri;
  }

  public void setBri(int bri) {
    this.bri = bri;
  }

  public int getHue() {
    return hue;
  }

  public void setHue(int hue) {
    this.hue = hue;
  }

  public int getSat() {
    return sat;
  }

  public void setSat(int sat) {
    this.sat = sat;
  }

  public String getEffect() {
    return effect;
  }

  public void setEffect(String effect) {
    this.effect = effect;
  }

  public List<Float> getXy() {
    return xy;
  }

  public void setXy(List<Float> xy) {
    this.xy = xy;
  }

  public int getCt() {
    return ct;
  }

  public void setCt(int ct) {
    this.ct = ct;
  }

  public String getAlert() {
    return alert;
  }

  public void setAlert(String alert) {
    this.alert = alert;
  }

  public String getColormode() {
    return colormode;
  }

  public void setColormode(String colormode) {
    this.colormode = colormode;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
