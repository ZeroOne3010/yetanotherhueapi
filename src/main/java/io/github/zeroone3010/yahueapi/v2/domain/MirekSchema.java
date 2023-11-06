package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MirekSchema {

  @JsonProperty("mirek_minimum")
  private int mirekMinimum;

  @JsonProperty("mirek_maximum")
  private int mirekMaximum;

  public int getMirekMinimum() {
    return mirekMinimum;
  }

  public int getMirekMaximum() {
    return mirekMaximum;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
