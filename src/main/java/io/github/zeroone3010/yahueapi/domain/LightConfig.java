package io.github.zeroone3010.yahueapi.domain;

public class LightConfig {
  private String archetype;
  private String function;
  private String direction;
  private Startup startup;

  public String getArchetype() {
    return archetype;
  }

  public String getFunction() {
    return function;
  }

  public String getDirection() {
    return direction;
  }

  public Startup getStartup() {
    return startup;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
