package io.github.zeroone3010.yahueapi.v2.domain.update;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum TimedEffectType {
  /**
   * A timed effect not supported by this version of this library.
   */
  @JsonEnumDefaultValue UNKNOWN,
  @JsonProperty("sunrise") SUNRISE,
  @JsonProperty("no_effect") NO_EFFECT
}
