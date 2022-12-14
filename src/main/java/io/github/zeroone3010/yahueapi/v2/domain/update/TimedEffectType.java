package io.github.zeroone3010.yahueapi.v2.domain.update;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TimedEffectType {
  @JsonProperty("sunrise") SUNRISE,
  @JsonProperty("no_effect") NO_EFFECT
}
