package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

public class Metadata {

  @JsonProperty("archetype")
  private String archetype;

  @JsonProperty("name")
  private String name;

  public String getArchetype() {
    return archetype;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
