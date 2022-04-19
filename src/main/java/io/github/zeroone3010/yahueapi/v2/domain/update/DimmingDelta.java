package io.github.zeroone3010.yahueapi.v2.domain.update;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DimmingDelta {

  @JsonProperty("action")
  private String action;

  @JsonProperty("brightness_delta")
  private int brightnessDelta;

  public String getAction() {
    return action;
  }

  /**
   * One of: "up", "down", "stop"
   *
   * @param action "up", "down" or "stop"
   */
  public void setAction(String action) {
    this.action = action;
  }

  public int getBrightnessDelta() {
    return brightnessDelta;
  }

  public void setBrightnessDelta(int brightnessDelta) {
    this.brightnessDelta = brightnessDelta;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
