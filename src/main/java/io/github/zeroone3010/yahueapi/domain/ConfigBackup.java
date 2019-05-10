package io.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfigBackup {
  @JsonProperty("status")
  private String status;
  @JsonProperty("errorcode")
  private int errorCode;

  public String getStatus() {
    return status;
  }

  public int getErrorCode() {
    return errorCode;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
