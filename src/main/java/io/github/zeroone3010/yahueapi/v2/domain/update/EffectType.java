package io.github.zeroone3010.yahueapi.v2.domain.update;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EffectType {
  @JsonProperty("fire") FIRE,
  @JsonProperty("candle") CANDLE,
  @JsonProperty("sparkle") SPARKLE,
  @JsonProperty("no_effect") NO_EFFECT;
}
