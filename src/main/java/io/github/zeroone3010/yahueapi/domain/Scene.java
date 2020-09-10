package io.github.zeroone3010.yahueapi.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class Scene {
  @SerializedName("name")
  private String name;
  @SerializedName("type")
  private String type;
  @SerializedName("group")
  private String group;
  @SerializedName("lights")
  private List<String> lights;
  @SerializedName("owner")
  private String owner;
  @SerializedName("recycle")
  private boolean recycle;
  @SerializedName("locked")
  private boolean locked;
  @SerializedName("appdata")
  private Map<String, Object> appdata;
  @SerializedName("picture")
  private String picture;
  @SerializedName("lastupdated")
  private String lastUpdated;
  @SerializedName("version")
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
