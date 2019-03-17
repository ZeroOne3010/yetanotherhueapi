package io.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DeviceTypes {
  @JsonProperty("bridge")
  private boolean bridge;
  @JsonProperty("lights")
  private List<Object> lights;
  @JsonProperty("sensors")
  private List<Object> sensors;

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
