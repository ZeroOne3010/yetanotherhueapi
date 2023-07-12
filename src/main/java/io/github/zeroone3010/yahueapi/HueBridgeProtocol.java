package io.github.zeroone3010.yahueapi;

/**
 * An enumeration on what kind of a connection one wants with the Bridge. Plain HTTP used to be an option but Philips
 * announced it will not be supported anymore.
 */
enum HueBridgeProtocol {

  /**
   * <b>Recommended</b>
   * <p>
   * An encrypted HTTPS connection. The Bridge certificate is verified against the stored
   * Root CA certificate issued by Signify. However, in the case of older Bridge models that still employ
   * self-signed certificates, this validation process will result in a failure.
   * @see <a href="https://developers.meethue.com/develop/application-design-guidance/using-https/">Hue Documentation</a>
   */
  HTTPS("https"),

  /**
   * An encrypted HTTPS connection. However, as older Bridges use a self-signed certificate,
   * it is not possible to verify it. Using this enum value turns off the certificate verification.
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
