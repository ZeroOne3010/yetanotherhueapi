package io.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WhiteListItem {
  @JsonProperty("last use date")
  private String lastUseDate;
  @JsonProperty("create date")
  private String createDate;
  @JsonProperty("name")
  private String name;

  public String getLastUseDate() {
    return lastUseDate;
  }

  public void setLastUseDate(String lastUseDate) {
    this.lastUseDate = lastUseDate;
  }

  public String getCreateDate() {
    return createDate;
  }

  public void setCreateDate(String createDate) {
    this.createDate = createDate;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
