package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BridgeConfig {
  @JsonProperty("name")
  private String name;
  @JsonProperty("mac")
  private String mac;

  public String getName() {
    return name;
  }

  public String getMac() {
    return mac;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
