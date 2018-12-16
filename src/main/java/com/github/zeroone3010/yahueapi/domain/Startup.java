package com.github.zeroone3010.yahueapi.domain;

public class Startup {
  private StartupMode mode;
  private boolean configured;

  public StartupMode getMode() {
    return mode;
  }

  public void setMode(StartupMode mode) {
    this.mode = mode;
  }

  public boolean isConfigured() {
    return configured;
  }

  public void setConfigured(boolean configured) {
    this.configured = configured;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
