package io.github.zeroone3010.yahueapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
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
    try {
      logger.debug("Turning off certificate verification...");
      final SSLContext sslContext = SSLContext.getInstance("SSL");
      sslContext.init(null, new TrustManager[]{new TrustEverythingManager()}, new SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
      HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
      logger.debug("Certificate verification has been turned off, all certificates are now accepted.");
    } catch (final NoSuchAlgorithmException | KeyManagementException e) {
      throw new HueApiException(e);
    }
  }

  public static HttpsURLConnection createAllTrustedConnection(URL url) throws IOException {
    try {
      SSLContext sslContext = SSLContext.getInstance("SSL");
      sslContext.init(null, new TrustManager[] {new TrustEverythingManager()}, null);

      HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
      connection.setHostnameVerifier((hostname, session) -> true);
      connection.setSSLSocketFactory(sslContext.getSocketFactory());
      connection.connect();
      return connection;
    } catch (GeneralSecurityException exception) {
      throw new HueApiException(exception);
    }
  }

}
