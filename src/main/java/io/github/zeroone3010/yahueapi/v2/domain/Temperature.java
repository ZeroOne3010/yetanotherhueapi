package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

import java.math.BigDecimal;

public class Temperature {
  @JsonProperty("temperature")
  private BigDecimal temperature;

  @JsonProperty("temperature_valid")
  private boolean temperatureValid;

  public BigDecimal getTemperature() {
    return temperature;
  }

  public boolean isTemperatureValid() {
    return temperatureValid;
  }

  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
