package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public class HueEvent {
  @JsonProperty("creationtime")
  private String creationTime;

  @JsonProperty("data")
  private List<HueEventData> data;

  @JsonProperty("id")
  private UUID id;

  @JsonProperty("type")
  private String type;

  public String getCreationTime() {
    return creationTime;
  }

  public List<HueEventData> getData() {
    return data;
  }

  public UUID getId() {
    return id;
  }

  public String getType() {
    return type;
  }

  @Override
  public String toString() {
    return "HueEvent{" +
        "creationTime='" + creationTime + '\'' +
        ", data=" + data +
        ", id=" + id +
        ", type='" + type + '\'' +
        '}';
  }
}
