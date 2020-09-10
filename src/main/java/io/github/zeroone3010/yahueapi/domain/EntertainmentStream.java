package io.github.zeroone3010.yahueapi.domain;

import com.google.gson.annotations.SerializedName;

public class EntertainmentStream {
  @SerializedName("proxymode")
  private String proxyMode;
  @SerializedName("proxynode")
  private String proxyNode;
  @SerializedName("active")
  private boolean active;
  @SerializedName("owner")
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
