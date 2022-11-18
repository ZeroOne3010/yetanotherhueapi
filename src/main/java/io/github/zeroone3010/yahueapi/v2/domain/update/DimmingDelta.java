package io.github.zeroone3010.yahueapi.v2.domain.update;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DimmingDelta {

  @JsonProperty("action")
  private DeltaAction action;

  @JsonProperty("brightness_delta")
  private int brightnessDelta;

  public DeltaAction getAction() {
    return action;
  }

  /**
   * Sets the dimming delta action and returns self.
   *
   * @param action Delta action
   * @return Self, so that one can also use this method like a fluent builder.
   */
  public DimmingDelta setAction(DeltaAction action) {
    this.action = action;
    return this;
  }

  public int getBrightnessDelta() {
    return brightnessDelta;
  }

  /**
   * Brightness percentage of full-scale increase delta to current dimlevel.
   * Clips at max-level or min-level.
   *
   * @param brightnessDelta A number, maximum value is 100.
   * @return Self, so that one can also use this method like a fluent builder.
   */
  public DimmingDelta setBrightnessDelta(int brightnessDelta) {
    this.brightnessDelta = brightnessDelta;
    return this;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
