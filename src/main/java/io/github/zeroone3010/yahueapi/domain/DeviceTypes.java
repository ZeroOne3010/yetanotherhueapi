package io.github.zeroone3010.yahueapi.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeviceTypes {
  @SerializedName("bridge")
  private boolean bridge;
  @SerializedName("lights")
  private List<Object> lights;
  @SerializedName("sensors")
  private List<Object> sensors;

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
