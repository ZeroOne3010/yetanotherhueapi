package io.github.zeroone3010.yahueapi.v2;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.github.zeroone3010.yahueapi.HueApiException;
import io.github.zeroone3010.yahueapi.SecureJsonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpUtil {
  private static final Logger logger = LoggerFactory.getLogger("io.github.zeroone3010.yahueapi");

  private HttpUtil() {
    // prevent instantiation
  }

  public static ObjectMapper buildObjectMapper(final String bridgeIp) {
    return JsonMapper.builder(new SecureJsonFactory(bridgeIp))
        .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true)
        .build();
  }

  public static String put(final Hue hue, final URL baseUrl, final String path, final String body) {
    return getString(hue::getUrlConnection, baseUrl, path, body, "PUT");
  }

  public static String post(final Hue hue, final URL baseUrl, final String path, final String body) {
    return getString(hue::getUrlConnection, baseUrl, path, body, "POST");
  }

  public static String post(final URL baseUrl, final String path, final String body) {
    return getString(HttpUtil::getAnonymousUrlConnection, baseUrl, path, body, "POST");
  }

  public static HttpsURLConnection getAnonymousUrlConnection(final URL url) {
    try {
      final HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
      final SecureJsonFactory factory = (SecureJsonFactory) buildObjectMapper(url.getHost()).getFactory();
      urlConnection.setSSLSocketFactory(factory.getSocketFactory());
      urlConnection.setHostnameVerifier(factory.getHostnameVerifier());
      return urlConnection;
    } catch (IOException e) {
      throw new HueApiException(e);
    }
  }

  private static String getString(final Function<URL, HttpsURLConnection> urlConnector,
                                  final URL baseUrl,
                                  final String path,
                                  final String body,
                                  final String method) {
    try {
      logger.trace("Request body: " + body);
      final HttpsURLConnection connection = urlConnector.apply(new URL(baseUrl.toString() + path));
      connection.setDoOutput(true);
      connection.setRequestMethod(method);
      connection.setRequestProperty("Host", connection.getURL().getHost());
      if (body != null) {
        try (final OutputStream outputStream = connection.getOutputStream()) {
          try (final OutputStreamWriter writer = new OutputStreamWriter(outputStream, UTF_8)) {
            writer.write(body);
            writer.flush();
          }
        }
      }
      connection.connect();
      try (final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
        return reader.lines().collect(Collectors.joining("\n"));
      }
    } catch (final IOException e) {
      throw new HueApiException(e);
    }
  }
}
