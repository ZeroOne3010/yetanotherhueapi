package io.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfigSoftwareUpdate2 {
  @JsonProperty("checkforupdate")
  private boolean checkforupdate;
  @JsonProperty("lastchange")
  private String lastchange;
  @JsonProperty("bridge")
  private ConfigSoftwareUpdate2Bridge bridge;
  @JsonProperty("state")
  private String state;
  @JsonProperty("autoinstall")
  private ConfigSoftwareUpdate2AutoInstall autoinstall;

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
