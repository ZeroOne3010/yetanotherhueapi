package io.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EntertainmentStream {
  @JsonProperty("proxymode")
  private String proxymode;
  @JsonProperty("proxynode")
  private String proxynode;
  @JsonProperty("active")
  private boolean active;
  @JsonProperty("owner")
  private String owner;

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
