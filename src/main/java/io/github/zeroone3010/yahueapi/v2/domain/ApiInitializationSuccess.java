package io.github.zeroone3010.yahueapi.v2.domain;

public class ApiInitializationSuccess {
  private String username;

  public String getUsername() {
    return username;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
