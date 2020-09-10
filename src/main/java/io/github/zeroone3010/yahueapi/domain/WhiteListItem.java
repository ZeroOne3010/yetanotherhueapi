package io.github.zeroone3010.yahueapi.domain;

import com.google.gson.annotations.SerializedName;

public class WhiteListItem {
  @SerializedName("last use date")
  private String lastUseDate;
  @SerializedName("create date")
  private String createDate;
  @SerializedName("name")
  private String name;

  public String getLastUseDate() {
    return lastUseDate;
  }

  public void setLastUseDate(final String lastUseDate) {
    this.lastUseDate = lastUseDate;
  }

  public String getCreateDate() {
    return createDate;
  }

  public void setCreateDate(final String createDate) {
    this.createDate = createDate;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
