package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

public class Sensitivity {

  @JsonProperty("status")
  private String status;

  @JsonProperty("sensitivity")
  private int sensitivity;

  @JsonProperty("sensitivity_max")
  private int maxSensitivity;

  public String getStatus() {
    return status;
  }

  public int getSensitivity() {
    return sensitivity;
  }

  public int getSensitivityMax() {
    return maxSensitivity;
  }

  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
