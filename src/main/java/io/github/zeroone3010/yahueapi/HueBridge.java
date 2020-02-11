package io.github.zeroone3010.yahueapi;

import java.util.Objects;

/**
 * A class representing a Hue Bridge in the application initialization phase.
 * Most people are likely to have just one of these.
 */
public class HueBridge {
  private final String name;
  private final String ip;
  private final String mac;

  public HueBridge(final String ip) {
    this(ip, ip, null);
  }

  public HueBridge(final String ip, final String name, final String mac) {
    this.ip = ip;
    this.name = name;
    this.mac = mac;
  }

  /**
   * Returns the human-readable name of the Bridge.
   *
   * @return The name of the Bridge.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the IP address of the Bridge.
   *
   * @return The IP address of the Bridge.
   */
  public String getIp() {
    return ip;
  }

  /**
   * Returns the MAC address of the Bridge.
   *
   * @return The MAC address of the Bridge.
   */
  public String getMac() {
    return mac;
  }

  @Override
  public String toString() {
    return "HueBridge{" +
        "name='" + name + '\'' +
        ", ip='" + ip + '\'' +
        ", mac='" + mac + '\'' +
        '}';
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final HueBridge hueBridge = (HueBridge) o;
    return Objects.equals(getIp(), hueBridge.getIp());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getIp());
  }
}
