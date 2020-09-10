package io.github.zeroone3010.yahueapi.domain;

import com.google.gson.annotations.SerializedName;

public class ConfigSoftwareUpdate2Bridge {
  @SerializedName("state")
  private String state;
  @SerializedName("lastinstall")
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
