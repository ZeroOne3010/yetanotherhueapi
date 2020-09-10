package io.github.zeroone3010.yahueapi.domain;

import com.google.gson.annotations.SerializedName;

public class ConfigSoftwareUpdate2AutoInstall {
  @SerializedName("updatetime")
  private String updateTime;
  @SerializedName("on")
  private boolean on;

  public String getUpdateTime() {
    return updateTime;
  }

  public boolean isOn() {
    return on;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
