package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

public class On {

  @JsonProperty("on")
  private boolean on;

  public boolean isOn() {
    return on;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
