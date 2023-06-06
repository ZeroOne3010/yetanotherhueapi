package io.github.zeroone3010.yahueapi.v2.domain.update;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum EffectType {
  /**
   * An effect not supported by this version of this library.
   */
  @JsonEnumDefaultValue UNKNOWN,

  @JsonProperty("fire") FIRE,
  @JsonProperty("candle") CANDLE,
  @JsonProperty("sparkle") SPARKLE,
  @JsonProperty("prism") PRISM,
  @JsonProperty("no_effect") NO_EFFECT;
}
