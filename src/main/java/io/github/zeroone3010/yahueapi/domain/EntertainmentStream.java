package io.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EntertainmentStream {
  @JsonProperty("proxymode")
  private String proxyMode;
  @JsonProperty("proxynode")
  private String proxyNode;
  @JsonProperty("active")
  private boolean active;
  @JsonProperty("owner")
  private String owner;

  public String getProxyMode() {
    return proxyMode;
  }

  public String getProxyNode() {
    return proxyNode;
  }

  public boolean isActive() {
    return active;
  }

  public String getOwner() {
    return owner;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
