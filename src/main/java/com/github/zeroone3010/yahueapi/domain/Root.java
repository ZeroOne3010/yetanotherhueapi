package com.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Map;

public class Root {

  @JsonProperty("lights")
  private Map<String, Light> lights;
  @JsonProperty("groups")
  private Map<String, Group> groups;
  @JsonProperty("config")
  private BridgeConfig config;
  @JsonIgnore
  private Map<String, Schedule> schedules;
  @JsonIgnore
  private Map<String, Scene> scenes;
  @JsonIgnore
  private Map<String, Rule> rules;
  @JsonProperty("sensors")
  private Map<String, Sensor> sensors;
  @JsonIgnore
  private Map<String, ResourceLink> resourcelinks;

  public Map<String, Light> getLights() {
    return lights;
  }

  public void setLights(Map<String, Light> lights) {
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

  public Map<String, Sensor> getSensors() {
    return sensors;
  }

  public void setSensors(Map<String, Sensor> sensors) {
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
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
