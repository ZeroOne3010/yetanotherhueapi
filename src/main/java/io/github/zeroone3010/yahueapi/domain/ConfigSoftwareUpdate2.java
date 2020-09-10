package io.github.zeroone3010.yahueapi.domain;

import com.google.gson.annotations.SerializedName;

public class ConfigSoftwareUpdate2 {
  @SerializedName("checkforupdate")
  private boolean checkForUpdate;
  @SerializedName("lastchange")
  private String lastChange;
  @SerializedName("bridge")
  private ConfigSoftwareUpdate2Bridge bridge;
  @SerializedName("state")
  private String state;
  @SerializedName("autoinstall")
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
