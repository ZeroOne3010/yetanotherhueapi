package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

public class Dimming {

  @JsonProperty("brightness")
  private int brightness;

  @JsonProperty("min_dim_level")
  private Integer minDimLevel;

  public int getBrightness() {
    return brightness;
  }

  public Integer getMinDimLevel() {
    return minDimLevel;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
