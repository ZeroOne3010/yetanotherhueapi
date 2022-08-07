package io.github.zeroone3010.yahueapi;

/**
 * An enumeration on what kind of a connection one wants with the Bridge. Plain HTTP used to be an option but Philips
 * announced it will not be supported anymore.
 */
enum HueBridgeProtocol {

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
