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

  public Xy setX(float x) {
    this.x = x;
    return this;
  }

  public float getY() {
    return y;
  }

  public Xy setY(float y) {
    this.y = y;
    return this;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
