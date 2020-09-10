package io.github.zeroone3010.yahueapi.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class Group {

  @SerializedName("name")
  private String name;
  @SerializedName("lights")
  private List<String> lights;
  @SerializedName("type")
  private String type;
  @SerializedName("state")
  private GroupState state;
  @SerializedName("recycle")
  private boolean recycle;
  @SerializedName("class")
  private String roomClass;
  @SerializedName("action")
  private Action action;
  @SerializedName("stream")
  private EntertainmentStream stream;
  @SerializedName("locations")
  private Map<String, List<Integer>> locations;

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public List<String> getLights() {
    return lights;
  }

  public void setLights(final List<String> lights) {
    this.lights = lights;
  }

  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public GroupState getState() {
    return state;
  }

  public void setState(final GroupState state) {
    this.state = state;
  }

  public boolean isRecycle() {
    return recycle;
  }

  public void setRecycle(final boolean recycle) {
    this.recycle = recycle;
  }

  public String getRoomClass() {
    return roomClass;
  }

  public void setRoomClass(final String roomClass) {
    this.roomClass = roomClass;
  }

  public Action getAction() {
    return action;
  }

  public void setAction(final Action action) {
    this.action = action;
  }

  public EntertainmentStream getStream() {
    return stream;
  }

  public void setStream(final EntertainmentStream stream) {
    this.stream = stream;
  }

  public Map<String, List<Integer>> getLocations() {
    return locations;
  }

  public void setLocations(final Map<String, List<Integer>> locations) {
    this.locations = locations;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
