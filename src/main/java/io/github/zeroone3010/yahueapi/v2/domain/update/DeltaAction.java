package io.github.zeroone3010.yahueapi.v2.domain.update;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum DeltaAction {
  @JsonProperty("up") UP,
  @JsonProperty("down") DOWN,
  @JsonProperty("stop") STOP;
}