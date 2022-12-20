package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LightLevel {
  @JsonProperty("light_level")
  private int lightLevel;

  @JsonProperty("light_level_valid")
  private boolean lightLevelValid;

  public int getLightLevel() {
    return lightLevel;
  }

  public boolean isLightLevelValid() {
    return lightLevelValid;
  }

  @Override
  public String toString() {
    return "LightLevel{" +
        "lightLevel=" + lightLevel +
        ", lightLevelValid=" + lightLevelValid +
        '}';
  }
}
