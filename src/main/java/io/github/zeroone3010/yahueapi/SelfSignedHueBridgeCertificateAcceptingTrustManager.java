package io.github.zeroone3010.yahueapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * A trust manager that validates, as well as it's possible, self-signed Hue Bridge certificates, while still
 * maintaining regular validation for other certificates.
 */
public class SelfSignedHueBridgeCertificateAcceptingTrustManager implements X509TrustManager {
  private static final Logger logger = LoggerFactory.getLogger("io.github.zeroone3010.yahueapi");

  public static final String PHILIPS_HUE_CERTIFICATE_ORGANIZATION = "O=Philips Hue,";
  private final X509TrustManager trustManager;

  SelfSignedHueBridgeCertificateAcceptingTrustManager(final X509TrustManager trustManager) {
    this.trustManager = trustManager;
  }

  @Override
  public X509Certificate[] getAcceptedIssuers() {
    return new X509Certificate[0];
  }

  @Override
  public void checkClientTrusted(final X509Certificate[] chain, final String authType) {
    throw new UnsupportedOperationException("This trust manager does not work for validating client certificates.");
  }

  @Override
  public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
    try {
      trustManager.checkServerTrusted(chain, authType);
    } catch (final CertificateException exception) {
      if (exception.getMessage() == null || !exception.getMessage().contains("path")) {
        // Don't know what's going on, better to reject the certificate.
        logger.debug("Certificate rejected, trust manager provided unknown exception");
        throw exception;
      }
      if (chain == null || chain.length != 1) {
        // Bridges with a self-signed cert do not provide a chain, just the one certificate.
        logger.debug("Certificate rejected, chain does not contain exactly one certificate");
        throw exception;
      }
      final X509Certificate serverCertificate = chain[0];
      final String certificateSerialNumber = serverCertificate.getSerialNumber().toString(16).toLowerCase();
      if (!serverCertificate.getIssuerDN().toString().contains(PHILIPS_HUE_CERTIFICATE_ORGANIZATION)) {
        // Self-signed Bridge certs should have the issuer name "Philips Hue".
        logger.debug("Certificate rejected, issuer DN is wrong: {}", serverCertificate.getIssuerDN());
        throw exception;
      }
      if (!serverCertificate.getIssuerDN().toString().contains(certificateSerialNumber)) {
        // Self-signed Bridge certs should have the serial number as their issuer DN.
        logger.debug("Certificate rejected, serial number {} does not match issuer DN {}",
            certificateSerialNumber,
            serverCertificate.getIssuerDN());
        throw exception;
      }
      if (!serverCertificate.getSubjectDN().toString().contains(PHILIPS_HUE_CERTIFICATE_ORGANIZATION)) {
        // Self-signed Bridge certs should have the subject name "Philips Hue".
        logger.debug("Certificate rejected, subject DN is wrong: {}", serverCertificate.getSubjectDN());
        throw exception;
      }
      if (!serverCertificate.getSubjectDN().toString().contains(certificateSerialNumber)) {
        // Self-signed Bridge certs should have the serial number as their subject common name.
        logger.debug("Certificate rejected, serial number {} does not match subject DN {}",
            certificateSerialNumber,
            serverCertificate.getSubjectDN());
        throw exception;
      }
    }
  }
}
