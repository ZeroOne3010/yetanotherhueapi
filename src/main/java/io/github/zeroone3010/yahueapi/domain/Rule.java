package io.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Rule {
  @JsonProperty("name")
  private String name;
  @JsonProperty("owner")
  private String owner;
  @JsonProperty("created")
  private String created;
  @JsonProperty("lasttriggered")
  private String lastTriggered;
  @JsonProperty("timestriggered")
  private int timesTriggered;
  @JsonProperty("status")
  private String status;
  @JsonProperty("recycle")
  private boolean recycle;
  @JsonProperty("conditions")
  private List<RuleCondition> conditions;
  @JsonProperty("actions")
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
