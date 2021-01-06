package io.github.zeroone3010.yahueapi.domain;

public class ComponentSoftwareUpdate {
  private String state;
  private String lastinstall;

  public String getState() {
    return state;
  }

  public String getLastinstall() {
    return lastinstall;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
