package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

public class ButtonSpecifics {
  @JsonProperty("last_event")
  private String lastEvent;

  /**
   * One of "initial_press", "repeat", "short_release", "long_release", "double_short_release".
   *
   * @return Last event type
   */
  public String getLastEvent() {
    return lastEvent;
  }

  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
