package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

public class Color {

  @JsonProperty("xy")
  private Xy xy;

  @JsonProperty("gamut")
  private Gamut gamut;

  @JsonProperty("gamut_type")
  private String gamutType;

  public Xy getXy() {
    return xy;
  }

  public Gamut getGamut() {
    return gamut;
  }

  public String getGamutType() {
    return gamutType;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }

}
