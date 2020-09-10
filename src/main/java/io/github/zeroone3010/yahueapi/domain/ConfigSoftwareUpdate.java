package io.github.zeroone3010.yahueapi.domain;


import com.google.gson.annotations.SerializedName;

public class ConfigSoftwareUpdate {
  @SerializedName("updatestate")
  private int updateState;
  @SerializedName("checkforupdate")
  private boolean checkForUpdate;
  @SerializedName("devicetypes")
  private DeviceTypes deviceTypes;
  @SerializedName("url")
  private String url;
  @SerializedName("text")
  private String text;
  @SerializedName("notify")
  private boolean notify;

  public int getUpdateState() {
    return updateState;
  }

  public boolean isCheckForUpdate() {
    return checkForUpdate;
  }

  public DeviceTypes getDeviceTypes() {
    return deviceTypes;
  }

  public String getUrl() {
    return url;
  }

  public String getText() {
    return text;
  }

  public boolean isNotify() {
    return notify;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
