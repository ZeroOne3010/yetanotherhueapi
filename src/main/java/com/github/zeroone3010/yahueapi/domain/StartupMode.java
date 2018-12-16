package com.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StartupMode {
  BRIGHT_LIGHT("safety"),
  KEEP_STATE("powerfail"),
  LAST_ON_STATE("lastonstate"),
  CUSTOM("custom"),
  UNKNOWN("unknown");

  private final String apiValue;

  StartupMode(final String apiValue) {
    this.apiValue = apiValue;
  }

  public String getApiValue() {
    return apiValue;
  }

  @JsonValue
  public String jsonValue() {
    return getApiValue();
  }
}
