package com.github.zeroone3010.yahueapi.domain;

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

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getLights() {
        return lights;
    }

    public void setLights(List<String> lights) {
        this.lights = lights;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public GroupState getState() {
        return state;
    }

    public void setState(GroupState state) {
        this.state = state;
    }

    public boolean isRecycle() {
        return recycle;
    }

    public void setRecycle(boolean recycle) {
        this.recycle = recycle;
    }

    public String getRoomClass() {
        return roomClass;
    }

    public void setRoomClass(String roomClass) {
        this.roomClass = roomClass;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public EntertainmentStream getStream() {
        return stream;
    }

    public void setStream(EntertainmentStream stream) {
        this.stream = stream;
    }

    public Map<String, List<Integer>> getLocations() {
        return locations;
    }

    public void setLocations(Map<String, List<Integer>> locations) {
        this.locations = locations;
    }

    @Override
    public String toString() {
        return JsonStringUtil.toJsonString(this);
    }
}
