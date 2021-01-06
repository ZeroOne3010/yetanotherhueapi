package io.github.zeroone3010.yahueapi.domain;

public class ApiInitializationError {
  private int type;
  private String address;
  private String description;

  public int getType() {
    return type;
  }

  public String getAddress() {
    return address;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
