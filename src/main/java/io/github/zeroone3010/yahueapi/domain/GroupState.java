package io.github.zeroone3010.yahueapi.domain;

import com.google.gson.annotations.SerializedName;

public class GroupState {
  @SerializedName("all_on")
  private boolean allOn;
  @SerializedName("any_on")
  private boolean anyOn;

  public boolean isAllOn() {
    return allOn;
  }

  public void setAllOn(final boolean allOn) {
    this.allOn = allOn;
  }

  public boolean isAnyOn() {
    return anyOn;
  }

  public void setAnyOn(final boolean anyOn) {
    this.anyOn = anyOn;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
