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
  @JsonIgnore
  private Map<String, Scene> scenes;
  @JsonProperty("rules")
  private Map<String, Rule> rules;
  @JsonProperty("sensors")
  private Map<String, SensorDto> sensors;
  @JsonIgnore
  private Map<String, ResourceLink> resourcelinks;

  public Map<String, LightDto> getLights() {
    return lights;
  }

  public void setLights(Map<String, LightDto> lights) {
    this.lights = lights;
  }

  public Map<String, Group> getGroups() {
    return groups;
  }

  public void setGroups(Map<String, Group> groups) {
    this.groups = groups;
  }

  public BridgeConfig getConfig() {
    return config;
  }

  public void setConfig(BridgeConfig config) {
    this.config = config;
  }

  public Map<String, Schedule> getSchedules() {
    return schedules;
  }

  public void setSchedules(Map<String, Schedule> schedules) {
    this.schedules = schedules;
  }

  public Map<String, Scene> getScenes() {
    return scenes;
  }

  public void setScenes(Map<String, Scene> scenes) {
    this.scenes = scenes;
  }

  public Map<String, Rule> getRules() {
    return rules;
  }

  public void setRules(Map<String, Rule> rules) {
    this.rules = rules;
  }

  public Map<String, SensorDto> getSensors() {
    return sensors;
  }

  public void setSensors(Map<String, SensorDto> sensors) {
    this.sensors = sensors;
  }

  public Map<String, ResourceLink> getResourcelinks() {
    return resourcelinks;
  }

  public void setResourcelinks(Map<String, ResourceLink> resourcelinks) {
    this.resourcelinks = resourcelinks;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
