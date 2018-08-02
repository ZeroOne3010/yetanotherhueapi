package com.github.zeroone3010.yahueapi.domain;

public class ApiInitializationSuccess {
  private String username;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
