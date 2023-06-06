package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("scene")
public class SceneResource extends Resource {

  @JsonProperty("group")
  private ResourceIdentifier group;
  @JsonProperty("metadata")
  private Metadata metadata;

  public ResourceIdentifier getGroup() {
    return group;
  }

  public Metadata getMetadata() {
    return metadata;
  }
}
