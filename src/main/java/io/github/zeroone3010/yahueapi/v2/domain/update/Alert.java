package io.github.zeroone3010.yahueapi.v2.domain.update;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Alert {

  @JsonProperty("action")
  private String action;

  public String getAction() {
    return action;
  }

  public Alert setAction(String action) {
    this.action = action;
    return this;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
