package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

public class GradientPointGet {

  @JsonProperty("color")
  private SimpleColor color;

  public SimpleColor getColor() {
    return color;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
