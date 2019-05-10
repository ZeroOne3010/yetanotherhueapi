package io.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfigSoftwareUpdate2Bridge {
  @JsonProperty("state")
  private String state;
  @JsonProperty("lastinstall")
  private String lastInstall;

  public String getState() {
    return state;
  }

  public String getLastInstall() {
    return lastInstall;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
