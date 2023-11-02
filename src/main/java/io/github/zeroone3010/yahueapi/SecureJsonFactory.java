package io.github.zeroone3010.yahueapi;

import com.fasterxml.jackson.databind.MappingJsonFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

public class SecureJsonFactory extends MappingJsonFactory {
  private final HostnameVerifier hostnameVerifier;
  private final SSLSocketFactory socketFactory;
  private X509TrustManager trustManager;

  public SecureJsonFactory(String bridgeIp) {
    try {
      this.socketFactory = createHueSSLContext().getSocketFactory();

      this.hostnameVerifier = (hostname, session) -> {
        if (bridgeIp == null) {
          return true;
        }
        final String bridgeHost = bridgeIp.contains(":") ? bridgeIp.split(":")[0] : bridgeIp;
        return hostname.equals(bridgeHost);
      };
    } catch (IOException | GeneralSecurityException exception) {
      throw new HueApiException(exception);
    }
  }

  @Override
  protected InputStream _optimizedStreamFromURL(URL url) throws IOException {
    // This is the default JsonFactory check for files
    if ("file".equals(url.getProtocol())) {
      String host = url.getHost();
      if (host == null || host.length() == 0) {
        String path = url.getPath();
        if (path.indexOf(37) < 0) {
          File file = new File(path);
          return Files.newInputStream(file.toPath());
        }
      }
    }
    // End of default check

    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
    connection.setSSLSocketFactory(socketFactory);
    connection.setHostnameVerifier(hostnameVerifier);
    connection.connect();
    return connection.getInputStream();
  }

  /**
   * Creates an SSL Context that only contains the Signify CA certificate of Hue bridges
   *
   * @return An SSL Context containing only the Hue certificate
   * @throws IOException              if the resource couldn't be read
   * @throws KeyStoreException        if the Java-Keystore Provider is missing
   * @throws CertificateException     if the X.509 certification type Provider is missing
   * @throws NoSuchAlgorithmException if no Provider supports the default TrustManager algorithm or the TLS Provider is missing
   * @throws KeyManagementException   if the SSLContext initiation fails
   * @see <a href="https://developers.meethue.com/develop/application-design-guidance/using-https/">Hue HTTPS Documentation</a>
   */
  protected SSLContext createHueSSLContext() throws IOException, KeyStoreException,
      CertificateException, NoSuchAlgorithmException, KeyManagementException {
    char[] passwordArray = new char[0]; //No password
    ClassLoader loader = getClass().getClassLoader();

    KeyStore keystore;
    try (InputStream certInputStream = loader.getResourceAsStream("hue_certificate.pem")) {
      keystore = KeyStore.getInstance("JKS");
      keystore.load(null, passwordArray);

      CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
      Certificate certificate = certificateFactory.generateCertificate(certInputStream);
      keystore.setCertificateEntry("PhilipsHue", certificate);
    }

    String trustManagerAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(trustManagerAlgorithm);

    SSLContext context = SSLContext.getInstance("TLS");
    trustManagerFactory.init(keystore);

    X509TrustManager defaultTrustManager = (X509TrustManager) trustManagerFactory.getTrustManagers()[0];
    this.trustManager = new SelfSignedHueBridgeCertificateAcceptingTrustManager(defaultTrustManager);
    context.init(null, new TrustManager[]{trustManager}, null);
    return context;
  }

  public SSLSocketFactory getSocketFactory() {
    return socketFactory;
  }

  public X509TrustManager getTrustManager() {
    return trustManager;
  }

  public HostnameVerifier getHostnameVerifier() {
    return hostnameVerifier;
  }
}
