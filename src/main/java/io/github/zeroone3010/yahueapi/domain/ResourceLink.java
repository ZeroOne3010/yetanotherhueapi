package io.github.zeroone3010.yahueapi.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResourceLink {
  @SerializedName("name")
  private String name;
  @SerializedName("description")
  private String description;
  @SerializedName("type")
  private String type;
  @SerializedName("classid")
  private int classid;
  @SerializedName("owner")
  private String owner;
  @SerializedName("recycle")
  private boolean recycle;
  @SerializedName("links")
  private List<String> links;


  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getType() {
    return type;
  }

  public int getClassid() {
    return classid;
  }

  public String getOwner() {
    return owner;
  }

  public boolean isRecycle() {
    return recycle;
  }

  public List<String> getLinks() {
    return links;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
