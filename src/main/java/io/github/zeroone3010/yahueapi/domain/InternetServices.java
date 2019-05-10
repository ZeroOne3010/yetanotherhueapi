package io.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InternetServices {
  @JsonProperty("internet")
  private String internet;
  @JsonProperty("remoteaccess")
  private String remoteAccess;
  @JsonProperty("time")
  private String time;
  @JsonProperty("swupdate")
  private String swupdate;

  public String getInternet() {
    return internet;
  }

  public String getRemoteAccess() {
    return remoteAccess;
  }

  public String getTime() {
    return time;
  }

  public String getSwupdate() {
    return swupdate;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
