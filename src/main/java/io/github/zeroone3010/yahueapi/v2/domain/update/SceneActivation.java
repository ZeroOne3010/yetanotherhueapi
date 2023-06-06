package io.github.zeroone3010.yahueapi.v2.domain.update;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SceneActivation {
  @JsonProperty("recall")
  private Recall recall = new Recall();

  public Recall getRecall() {
    return recall;
  }
}
