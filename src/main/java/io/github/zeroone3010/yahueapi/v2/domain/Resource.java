package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

import java.util.UUID;

public abstract class Resource {

  @JsonProperty("type")
  private String type;

  @JsonProperty("id")
  private UUID id;

  @JsonProperty("id_v1")
  private String idV1;

  public String getType() {
    return type;
  }

  public UUID getId() {
    return id;
  }

  public String getIdV1() {
    return idV1;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
