package io.github.zeroone3010.yahueapi.domain;


import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Root {

  @SerializedName("lights")
  private Map<String, LightDto> lights;
  @SerializedName("groups")
  private Map<String, Group> groups;
  @SerializedName("config")
  private BridgeConfig config;
  private transient Map<String, Schedule> schedules;
  @SerializedName("scenes")
  private Map<String, Scene> scenes;
  @SerializedName("rules")
  private Map<String, Rule> rules;
  @SerializedName("sensors")
  private Map<String, SensorDto> sensors;
  @SerializedName("resourcelinks")
  private Map<String, ResourceLink> resourcelinks;

  public Map<String, LightDto> getLights() {
    return lights;
  }

  public void setLights(final Map<String, LightDto> lights) {
    this.lights = lights;
  }

  public Map<String, Group> getGroups() {
    return groups;
  }

  public void setGroups(final Map<String, Group> groups) {
    this.groups = groups;
  }

  public BridgeConfig getConfig() {
    return config;
  }

  public void setConfig(final BridgeConfig config) {
    this.config = config;
  }

  public Map<String, Schedule> getSchedules() {
    return schedules;
  }

  public void setSchedules(final Map<String, Schedule> schedules) {
    this.schedules = schedules;
  }

  public Map<String, Scene> getScenes() {
    return scenes;
  }

  public void setScenes(final Map<String, Scene> scenes) {
    this.scenes = scenes;
  }

  public Map<String, Rule> getRules() {
    return rules;
  }

  public void setRules(final Map<String, Rule> rules) {
    this.rules = rules;
  }

  public Map<String, SensorDto> getSensors() {
    return sensors;
  }

  public void setSensors(final Map<String, SensorDto> sensors) {
    this.sensors = sensors;
  }

  public Map<String, ResourceLink> getResourcelinks() {
    return resourcelinks;
  }

  public void setResourcelinks(final Map<String, ResourceLink> resourcelinks) {
    this.resourcelinks = resourcelinks;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
