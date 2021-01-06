package io.github.zeroone3010.yahueapi.domain;

public class Startup {
  private StartupMode mode;
  private boolean configured;

  public StartupMode getMode() {
    return mode;
  }

  public boolean isConfigured() {
    return configured;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
