package io.github.zeroone3010.yahueapi.domain;

import com.google.gson.annotations.SerializedName;

public class ConfigBackup {
  @SerializedName("status")
  private String status;
  @SerializedName("errorcode")
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
