package io.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SensorCapabilities {
  @JsonProperty("certified")
  private boolean certified;

  @JsonProperty("primary")
  private boolean primary;

  @JsonProperty("inputs")
  private List<SensorInput> inputs;

  public boolean isCertified() {
    return certified;
  }

  public boolean isPrimary() {
    return primary;
  }

  public List<SensorInput> getInputs() {
    return inputs;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
