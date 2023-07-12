package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

import java.util.List;

public class RoomResourceRoot {

  @JsonProperty("errors")
  private List<Error> errors;

  @JsonProperty("data")
  private List<RoomResource> data;

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
