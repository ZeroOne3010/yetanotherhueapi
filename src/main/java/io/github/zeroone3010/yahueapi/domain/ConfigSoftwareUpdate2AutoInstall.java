package io.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfigSoftwareUpdate2AutoInstall {
  @JsonProperty("updatetime")
  private String updateTime;
  @JsonProperty("on")
  private boolean on;

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
