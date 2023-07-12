package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("grouped_light")
public class GroupedLightResource extends Resource {

  @JsonProperty("on")
  private On on;

  @JsonProperty("alert")
  private Alert alert;

  public On getOn() {
    return on;
  }

  public Alert getAlert() {
    return alert;
  }
}
