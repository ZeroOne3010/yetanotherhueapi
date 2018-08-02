package com.github.zeroone3010.yahueapi.domain;

public class LightConfig {
  private String archetype;
  private String function;
  private String direction;

  public String getArchetype() {
    return archetype;
  }

  public void setArchetype(String archetype) {
    this.archetype = archetype;
  }

  public String getFunction() {
    return function;
  }

  public void setFunction(String function) {
    this.function = function;
  }

  public String getDirection() {
    return direction;
  }

  public void setDirection(String direction) {
    this.direction = direction;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
