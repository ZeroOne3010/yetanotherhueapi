package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("bridge")
public class BridgeResource extends Resource {
  @JsonProperty("bridge_id")
  private String bridgeId;

  public String getBridgeId() {
    return bridgeId;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
