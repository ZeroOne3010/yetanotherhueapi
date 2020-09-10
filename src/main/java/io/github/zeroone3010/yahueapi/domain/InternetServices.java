package io.github.zeroone3010.yahueapi.domain;

import com.google.gson.annotations.SerializedName;

public class InternetServices {
  @SerializedName("internet")
  private String internet;
  @SerializedName("remoteaccess")
  private String remoteAccess;
  @SerializedName("time")
  private String time;
  @SerializedName("swupdate")
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
