package io.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RuleCondition {
  @JsonProperty("address")
  private String address;
  @JsonProperty("operator")
  private String operator;
  @JsonProperty("value")
  private String value;

  public String getAddress() {
    return address;
  }

  public String getOperator() {
    return operator;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
