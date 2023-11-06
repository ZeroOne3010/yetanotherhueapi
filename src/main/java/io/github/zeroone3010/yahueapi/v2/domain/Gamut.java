package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Gamut {

  @JsonProperty("red")
  private Xy red;

  @JsonProperty("green")
  private Xy green;

  @JsonProperty("blue")
  private Xy blue;

  public Xy getRed() {
    return red;
  }

  public Xy getGreen() {
    return green;
  }

  public Xy getBlue() {
    return blue;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
