package io.github.zeroone3010.yahueapi;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;

public class TrustEverythingManager implements X509TrustManager {

  public X509Certificate[] getAcceptedIssuers() {
    return new X509Certificate[]{};
  }

  public void checkClientTrusted(final X509Certificate[] certs, final String authType) {
    // Do nothing
  }

  public void checkServerTrusted(final X509Certificate[] certs, final String authType) {
    // Do nothing
  }

  public static SSLSocketFactory createSSLSocketFactory() throws GeneralSecurityException {
    SSLContext sslContext = SSLContext.getInstance("SSL");
    sslContext.init(null, new TrustManager[] {new TrustEverythingManager()}, null);
    return sslContext.getSocketFactory();
  }

  public static HostnameVerifier createHostnameVerifier(String bridgeIp) {
    return (hostname, session) -> bridgeIp == null || hostname.equals(bridgeIp);
  }

  public static X509TrustManager getTrustEverythingTrustManager() {
    return new TrustEverythingManager();
  }

  public static HttpsURLConnection createAllTrustedConnection(URL url) throws IOException {
    try {
      HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
      connection.setHostnameVerifier(createHostnameVerifier(null));
      connection.setSSLSocketFactory(createSSLSocketFactory());
      return connection;
    } catch (GeneralSecurityException exception) {
      throw new HueApiException(exception);
    }
  }

}
