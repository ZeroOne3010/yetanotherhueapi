package io.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SensorInput {
  @JsonProperty("repeatintervals")
  private List<Integer> repeatIntervals;

  @JsonProperty("events")
  private List<SensorEvent> events;

  public List<Integer> getRepeatIntervals() {
    return repeatIntervals;
  }

  public List<SensorEvent> getEvents() {
    return events;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
