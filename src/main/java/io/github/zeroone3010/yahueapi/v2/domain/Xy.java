package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

public class Xy {

  @JsonProperty("x")
  private float x;

  @JsonProperty("y")
  private float y;

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
