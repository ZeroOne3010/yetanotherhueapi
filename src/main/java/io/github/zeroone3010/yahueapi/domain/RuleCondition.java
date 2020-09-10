package io.github.zeroone3010.yahueapi.domain;

import com.google.gson.annotations.SerializedName;

public class RuleCondition {
  @SerializedName("address")
  private String address;
  @SerializedName("operator")
  private String operator;
  @SerializedName("value")
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
