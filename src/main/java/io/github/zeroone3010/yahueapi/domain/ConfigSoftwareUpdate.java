package io.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfigSoftwareUpdate {
  @JsonProperty("updatestate")
  private int updateState;
  @JsonProperty("checkforupdate")
  private boolean checkForUpdate;
  @JsonProperty("devicetypes")
  private DeviceTypes deviceTypes;
  @JsonProperty("url")
  private String url;
  @JsonProperty("text")
  private String text;
  @JsonProperty("notify")
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
