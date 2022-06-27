package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

public class ButtonMetadata {
  @JsonProperty("control_id")
  private int controlId;

  /**
   * minimum: 0 – maximum: 8
   *
   * @return Number of the button on the switch device.
   */
  public int getControlId() {
    return controlId;
  }

  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
