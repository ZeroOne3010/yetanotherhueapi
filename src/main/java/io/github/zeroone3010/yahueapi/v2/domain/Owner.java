package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

import java.util.UUID;

public class Owner {

  @JsonProperty("rid")
  private UUID rid;

  @JsonProperty("rtype")
  private String rtype;

  public UUID getRid() {
    return rid;
  }

  public String getRtype() {
    return rtype;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}