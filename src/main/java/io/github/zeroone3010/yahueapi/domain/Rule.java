package io.github.zeroone3010.yahueapi.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Rule {
  @SerializedName("name")
  private String name;
  @SerializedName("owner")
  private String owner;
  @SerializedName("created")
  private String created;
  @SerializedName("lasttriggered")
  private String lastTriggered;
  @SerializedName("timestriggered")
  private int timesTriggered;
  @SerializedName("status")
  private String status;
  @SerializedName("recycle")
  private boolean recycle;
  @SerializedName("conditions")
  private List<RuleCondition> conditions;
  @SerializedName("actions")
  private List<RuleAction> actions;

  public String getName() {
    return name;
  }

  public String getOwner() {
    return owner;
  }

  public String getCreated() {
    return created;
  }

  public String getLastTriggered() {
    return lastTriggered;
  }

  public int getTimesTriggered() {
    return timesTriggered;
  }

  public String getStatus() {
    return status;
  }

  public boolean isRecycle() {
    return recycle;
  }

  public List<RuleCondition> getConditions() {
    return conditions;
  }

  public List<RuleAction> getActions() {
    return actions;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
