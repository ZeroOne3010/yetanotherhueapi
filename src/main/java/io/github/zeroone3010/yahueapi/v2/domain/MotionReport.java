package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

public class MotionReport {
  @JsonProperty("changed")
  private String changed;

  @JsonProperty("motion")
  private boolean motion;

  public String getChanged() {
    return changed;
  }

  public boolean isMotion() {
    return motion;
  }

  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
