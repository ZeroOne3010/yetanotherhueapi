package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleColor {

  @JsonProperty("xy")
  private Xy xy;

  public Xy getXy() {
    return xy;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }

}
