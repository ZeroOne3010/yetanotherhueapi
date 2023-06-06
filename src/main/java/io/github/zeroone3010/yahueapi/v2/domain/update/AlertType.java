package io.github.zeroone3010.yahueapi.v2.domain.update;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum AlertType {
  /**
   * An alert type not supported by this version of this library.
   */
  @JsonEnumDefaultValue UNKNOWN,
  @JsonProperty("breathe") BREATHE
}
