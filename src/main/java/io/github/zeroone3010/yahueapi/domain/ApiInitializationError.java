package io.github.zeroone3010.yahueapi.domain;

public class ApiInitializationError {
  private int type;
  private String address;
  private String description;

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
