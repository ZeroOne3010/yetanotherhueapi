package io.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class Scene {
  @JsonProperty("name")
  private String name;
  @JsonProperty("type")
  private String type;
  @JsonProperty("group")
  private String group;
  @JsonProperty("lights")
  private List<String> lights;
  @JsonProperty("owner")
  private String owner;
  @JsonProperty("recycle")
  private boolean recycle;
  @JsonProperty("locked")
  private boolean locked;
  @JsonProperty("appdata")
  private Map<String, Object> appdata;
  @JsonProperty("picture")
  private String picture;
  @JsonProperty("lastupdated")
  private String lastUpdated;
  @JsonProperty("version")
  private int version;

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public String getGroup() {
    return group;
  }

  public List<String> getLights() {
    return lights;
  }

  public String getOwner() {
    return owner;
  }

  public boolean isRecycle() {
    return recycle;
  }

  public boolean isLocked() {
    return locked;
  }

  public Map<String, Object> getAppdata() {
    return appdata;
  }

  public String getPicture() {
    return picture;
  }

  public String getLastUpdated() {
    return lastUpdated;
  }

  public int getVersion() {
    return version;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
