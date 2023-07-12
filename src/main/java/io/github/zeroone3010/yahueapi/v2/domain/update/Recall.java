package io.github.zeroone3010.yahueapi.v2.domain.update;

import com.fasterxml.jackson.annotation.JsonProperty;

import static io.github.zeroone3010.yahueapi.v2.domain.update.SceneAction.ACTIVE;

public class Recall {
  @JsonProperty("action")
  private SceneAction action = ACTIVE;

  public SceneAction getAction() {
    return action;
  }
}
