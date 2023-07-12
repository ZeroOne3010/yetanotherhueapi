package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.v2.domain.update.EffectType;

import java.util.List;

public class Effects {
  @JsonProperty("effect")
  private EffectType effect;

  @JsonProperty("status_values")
  private List<EffectType> statusValues;

  @JsonProperty("status")
  private EffectType status;

  @JsonProperty("effect_values")
  private List<EffectType> effectValues;

  public EffectType getEffect() {
    return effect;
  }

  public List<EffectType> getStatusValues() {
    return statusValues;
  }

  public EffectType getStatus() {
    return status;
  }

  public List<EffectType> getEffectValues() {
    return effectValues;
  }
}
