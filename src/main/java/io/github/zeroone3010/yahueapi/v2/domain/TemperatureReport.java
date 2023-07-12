package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

import java.math.BigDecimal;

public class TemperatureReport {
  @JsonProperty("changed")
  private String changed;

  @JsonProperty("temperature")
  private BigDecimal temperature;

  public String getChanged() {
    return changed;
  }

  public BigDecimal getTemperature() {
    return temperature;
  }

  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
