package io.github.zeroone3010.yahueapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class TrustEverythingManager implements X509TrustManager {
  private static final Logger logger = LoggerFactory.getLogger(TrustEverythingManager.class);

  public X509Certificate[] getAcceptedIssuers() {
    return new X509Certificate[]{};
  }

  public void checkClientTrusted(final X509Certificate[] certs, final String authType) {
    // Do nothing
  }

  public void checkServerTrusted(final X509Certificate[] certs, final String authType) {
    // Do nothing
  }

  public static void trustAllSslConnectionsByDisablingCertificateVerification() {
    logger.debug("Turning off certificate verification...");
    HttpsURLConnection.setDefaultSSLSocketFactory(getTrustEverythingSocketFactory());
    HttpsURLConnection.setDefaultHostnameVerifier(getTrustEverythingHostnameVerifier());
    logger.debug("Certificate verification has been turned off, all certificates are now accepted.");
  }

  public static HostnameVerifier getTrustEverythingHostnameVerifier() {
    return (hostname, session) -> true;
  }

  public static X509TrustManager getTrustEverythingTrustManager() {
    return new TrustEverythingManager();
  }

  public static SSLSocketFactory getTrustEverythingSocketFactory() {
    try {
      final SSLContext sslContext = SSLContext.getInstance("SSL");
      sslContext.init(null, new TrustManager[]{getTrustEverythingTrustManager()}, new SecureRandom());
      return sslContext.getSocketFactory();
    } catch (final Exception e) {
      throw new HueApiException(e);
    }
  }
}
