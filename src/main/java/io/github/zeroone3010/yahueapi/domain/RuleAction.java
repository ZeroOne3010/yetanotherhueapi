package io.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class RuleAction {
  @JsonProperty("address")
  private String address;
  @JsonProperty("method")
  private String method;
  @JsonProperty("body")
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
