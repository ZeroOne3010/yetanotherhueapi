package io.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SensorEvent {
  @JsonProperty("buttonevent")
  private Integer buttonEvent;

  @JsonProperty("eventtype")
  private String eventType;

  public Integer getButtonEvent() {
    return buttonEvent;
  }

  public String getEventType() {
    return eventType;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
