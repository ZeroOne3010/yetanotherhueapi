package io.github.zeroone3010.yahueapi.v2.domain.update;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class On {

  @JsonProperty("on")
  private boolean on;

  public On(boolean on) {
    this.on = on;
  }

  public boolean isOn() {
    return on;
  }

  /**
   * Toggles the light on or off.
   *
   * @param on True for on, false for off.
   * @return Self, so that one can also use this method like a fluent builder.
   */
  public On setOn(boolean on) {
    this.on = on;
    return this;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
