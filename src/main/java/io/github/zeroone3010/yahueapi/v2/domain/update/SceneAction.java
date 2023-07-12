package io.github.zeroone3010.yahueapi.v2.domain.update;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SceneAction {
  @JsonProperty("active") ACTIVE,
  @JsonProperty("dynamic_palette") DYNAMIC_PALETTE,
  @JsonProperty("static") STATIC
}
