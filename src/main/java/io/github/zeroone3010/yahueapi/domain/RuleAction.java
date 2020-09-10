package io.github.zeroone3010.yahueapi.domain;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class RuleAction {
  @SerializedName("address")
  private String address;
  @SerializedName("method")
  private String method;
  @SerializedName("body")
  private Map<String, Object> body;

  public String getAddress() {
    return address;
  }

  public String getMethod() {
    return method;
  }

  public Map<String, Object> getBody() {
    return body;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
