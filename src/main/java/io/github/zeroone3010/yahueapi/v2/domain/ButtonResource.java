package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

import java.util.UUID;

public class ButtonResource {
  @JsonProperty("type")
  private String type;

  @JsonProperty("id")
  private UUID id;

  @JsonProperty("id_v1")
  private String idV1;

  @JsonProperty("owner")
  private Owner owner;

  @JsonProperty("metadata")
  private ButtonMetadata metadata;

  @JsonProperty("button")
  private ButtonSpecifics button;

  public String getType() {
    return type;
  }

  public UUID getId() {
    return id;
  }

  public String getIdV1() {
    return idV1;
  }

  public Owner getOwner() {
    return owner;
  }

  public ButtonMetadata getMetadata() {
    return metadata;
  }

  public ButtonSpecifics getButton() {
    return button;
  }

  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
