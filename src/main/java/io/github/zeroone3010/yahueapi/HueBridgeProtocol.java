package io.github.zeroone3010.yahueapi;

public enum HueBridgeProtocol {

  /**
   * A regular HTTP connection.
   */
  HTTP("http"),

  /**
   * An encrypted HTTPS connection. However, as the Bridge uses a self-signed certificate,
   * it is not possible to verify it. Using this enum value turns off the certificate
   * verification.
   */
  UNVERIFIED_HTTPS("https");

  private final String protocol;

  HueBridgeProtocol(final String protocol) {
    this.protocol = protocol;
  }

  String getProtocol() {
    return protocol;
  }
}
