package io.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Root {

  @JsonProperty("lights")
  private Map<String, LightDto> lights;
  @JsonProperty("groups")
  private Map<String, Group> groups;
  @JsonProperty("config")
  private BridgeConfig config;
  @JsonIgnore
  private Map<String, Schedule> schedules;
  @JsonProperty("scenes")
  private Map<String, Scene> scenes;
  @JsonProperty("rules")
  private Map<String, Rule> rules;
  @JsonProperty("sensors")
  private Map<String, SensorDto> sensors;
  @JsonProperty("resourcelinks")
  private Map<String, ResourceLink> resourcelinks;

  public Map<String, LightDto> getLights() {
    return lights;
  }

  public Map<String, Group> getGroups() {
    return groups;
  }

  public BridgeConfig getConfig() {
    return config;
  }

  public Map<String, Schedule> getSchedules() {
    return schedules;
  }

  public Map<String, Scene> getScenes() {
    return scenes;
  }

  public Map<String, Rule> getRules() {
    return rules;
  }

  public Map<String, SensorDto> getSensors() {
    return sensors;
  }

  public Map<String, ResourceLink> getResourcelinks() {
    return resourcelinks;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
