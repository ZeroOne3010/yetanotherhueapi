package io.github.zeroone3010.yahueapi;

import java.util.Objects;

/**
 * A class representing a Hue Bridge in the application initialization phase.
 * Most people are likely to have just one of these.
 */
public class HueBridge {
  private final String name;
  private final String ip;

  public HueBridge(final String ip) {
    this.name = ip;
    this.ip = ip;
  }

  public HueBridge(final String name, final String ip) {
    this.name = name;
    this.ip = ip;
  }

  public String getName() {
    return name;
  }

  public String getIp() {
    return ip;
  }

  @Override
  public String toString() {
    return "HueBridge{" +
        "name='" + name + '\'' +
        ", ip='" + ip + '\'' +
        '}';
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final HueBridge hueBridge = (HueBridge) o;
    return Objects.equals(getName(), hueBridge.getName()) &&
        Objects.equals(getIp(), hueBridge.getIp());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName(), getIp());
  }
}
