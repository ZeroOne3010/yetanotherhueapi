package io.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class Group {

  @JsonProperty("name")
  private String name;
  @JsonProperty("lights")
  private List<String> lights;
  @JsonProperty("type")
  private String type;
  @JsonProperty("state")
  private GroupState state;
  @JsonProperty("recycle")
  private boolean recycle;
  @JsonProperty("class")
  private String roomClass;
  @JsonProperty("action")
  private Action action;
  @JsonProperty("stream")
  private EntertainmentStream stream;
  @JsonProperty("locations")
  private Map<String, List<Integer>> locations;

  public String getName() {
    return name;
  }

  public List<String> getLights() {
    return lights;
  }

  public String getType() {
    return type;
  }

  public GroupState getState() {
    return state;
  }

  public boolean isRecycle() {
    return recycle;
  }

  public String getRoomClass() {
    return roomClass;
  }

  public Action getAction() {
    return action;
  }

  public EntertainmentStream getStream() {
    return stream;
  }

  public Map<String, List<Integer>> getLocations() {
    return locations;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
