package io.github.zeroone3010.yahueapi.v2.domain.update;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum DeltaAction {
  /**
   * A delta action not supported by this version of this library.
   */
  @JsonEnumDefaultValue UNKNOWN,
  @JsonProperty("up") UP,
  @JsonProperty("down") DOWN,
  @JsonProperty("stop") STOP;
}
