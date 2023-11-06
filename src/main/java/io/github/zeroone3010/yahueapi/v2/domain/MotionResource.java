package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MotionResource extends Resource {
  @JsonProperty("enabled")
  private boolean enabled;

  @JsonProperty("motion")
  private Motion motion;

  @JsonProperty("sensitivity")
  private Sensitivity sensitivity;

  public boolean isEnabled() {
    return enabled;
  }

  public Motion getMotion() {
    return motion;
  }

  public Sensitivity getSensitivity() {
    return sensitivity;
  }

  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
