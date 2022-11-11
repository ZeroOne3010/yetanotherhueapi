package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

import java.util.UUID;

public class ResourceIdentifier {

  @JsonProperty("rid")
  private UUID rid;

  @JsonProperty("rtype")
  private ResourceType rtype;

  public UUID getRid() {
    return rid;
  }

  @JsonProperty("rtype")
  public ResourceType getResourceType() {
    return rtype;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
