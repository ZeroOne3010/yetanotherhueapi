package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("button")
public class ButtonResource extends Resource {

  @JsonProperty("owner")
  private ResourceIdentifier owner;

  @JsonProperty("metadata")
  private ButtonMetadata metadata;

  @JsonProperty("button")
  private ButtonSpecifics button;

  public ButtonMetadata getMetadata() {
    return metadata;
  }

  public ButtonSpecifics getButton() {
    return button;
  }
}
