package io.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ResourceLink {
  @JsonProperty("name")
  private String name;
  @JsonProperty("description")
  private String description;
  @JsonProperty("type")
  private String type;
  @JsonProperty("classid")
  private int classid;
  @JsonProperty("owner")
  private String owner;
  @JsonProperty("recycle")
  private boolean recycle;
  @JsonProperty("links")
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
