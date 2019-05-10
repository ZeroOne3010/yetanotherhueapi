package io.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfigSoftwareUpdate2 {
  @JsonProperty("checkforupdate")
  private boolean checkForUpdate;
  @JsonProperty("lastchange")
  private String lastChange;
  @JsonProperty("bridge")
  private ConfigSoftwareUpdate2Bridge bridge;
  @JsonProperty("state")
  private String state;
  @JsonProperty("autoinstall")
  private ConfigSoftwareUpdate2AutoInstall autoInstall;

  public boolean isCheckForUpdate() {
    return checkForUpdate;
  }

  public String getLastChange() {
    return lastChange;
  }

  public ConfigSoftwareUpdate2Bridge getBridge() {
    return bridge;
  }

  public String getState() {
    return state;
  }

  public ConfigSoftwareUpdate2AutoInstall getAutoInstall() {
    return autoInstall;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
