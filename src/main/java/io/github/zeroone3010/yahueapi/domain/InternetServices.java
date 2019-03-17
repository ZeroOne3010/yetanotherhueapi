package io.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InternetServices {
  @JsonProperty("internet")
  private String internet;
  @JsonProperty("remoteaccess")
  private String remoteaccess;
  @JsonProperty("time")
  private String time;
  @JsonProperty("swupdate")
  private String swupdate;

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
